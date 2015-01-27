/**
 * 
 */
package es.caib.ripea.plugin.caib.portafirmes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.attachments.OctetStream;
import org.apache.axis.attachments.OctetStreamDataSource;
import org.apache.commons.io.IOUtils;

import es.caib.ripea.plugin.SistemaExternException;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocument;
import es.caib.ripea.plugin.portafirmes.PortafirmesDocumentTipus;
import es.caib.ripea.plugin.portafirmes.PortafirmesFluxBloc;
import es.caib.ripea.plugin.portafirmes.PortafirmesPlugin;
import es.caib.ripea.plugin.portafirmes.PortafirmesPrioritatEnum;
import es.caib.ripea.plugin.utils.PropertiesHelper;
import es.indra.www.portafirmasws.cws.Annex;
import es.indra.www.portafirmasws.cws.Application;
import es.indra.www.portafirmasws.cws.CWSSoapBindingStub;
import es.indra.www.portafirmasws.cws.Cws;
import es.indra.www.portafirmasws.cws.CwsServiceLocator;
import es.indra.www.portafirmasws.cws.DeleteRequest;
import es.indra.www.portafirmasws.cws.DeleteRequestDocument;
import es.indra.www.portafirmasws.cws.DeleteResponse;
import es.indra.www.portafirmasws.cws.DocumentAttributes;
import es.indra.www.portafirmasws.cws.DownloadRequest;
import es.indra.www.portafirmasws.cws.DownloadRequestDocument;
import es.indra.www.portafirmasws.cws.DownloadResponse;
import es.indra.www.portafirmasws.cws.ImportanceEnum;
import es.indra.www.portafirmasws.cws.Result;
import es.indra.www.portafirmasws.cws.Sender;
import es.indra.www.portafirmasws.cws.SignModeEnum;
import es.indra.www.portafirmasws.cws.Signer;
import es.indra.www.portafirmasws.cws.StateEnum;
import es.indra.www.portafirmasws.cws.Step;
import es.indra.www.portafirmasws.cws.Steps;
import es.indra.www.portafirmasws.cws.UploadRequest;
import es.indra.www.portafirmasws.cws.UploadRequestDocument;
import es.indra.www.portafirmasws.cws.UploadResponse;
import es.indra.www.portafirmasws.cws.UploadStep;

/**
 * Implementació del plugin de portafirmes emprant el portafirmes
 * de la CAIB desenvolupat per Indra. L'accés al servei web del
 * portafirmes empra la llibreria AXIS versió 1.4 i te dependències
 * amb javax.activation i javax.mail.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class PortafirmesPluginCwsAxis implements PortafirmesPlugin {

	@Override
	public long upload(
			PortafirmesDocument document,
			Long documentTipus,
			String motiu,
			String remitent,
			PortafirmesPrioritatEnum importancia,
			Date dataCaducitat,
			List<PortafirmesFluxBloc> flux,
			Long plantillaFluxId,
			List<PortafirmesDocument> annexos,
			boolean signarAnnexos) throws SistemaExternException {
		try {
			comprovarExtensioArxiu(document.getArxiuExtensio());
			UploadRequest uploadRequest = new UploadRequest();
			Application application = new Application();
			application.setUser(getUsername());
			application.setPassword(getPassword());
			uploadRequest.setApplication(application);
			uploadRequest.setDocument(getUploadRequestDocument(
					document,
					documentTipus,
					annexos,
					signarAnnexos,
					flux,
					motiu,
					remitent,
					importancia,
					dataCaducitat));
			Cws cws = getCwsService();
			addAttachments(cws, document, annexos);
			UploadResponse uploadResponse = cws.uploadDocument(
					uploadRequest);
			comprovarResult(uploadResponse.getResult());
			return uploadResponse.getDocument().getId();
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut pujar el document al portafirmes",
					ex);
		}
	}

	@Override
	public PortafirmesDocument download(
			long id) throws SistemaExternException {
		try {
			DownloadRequest downloadRequest = new DownloadRequest();
			Application application = new Application();
			application.setUser(getUsername());
			application.setPassword(getPassword());
			downloadRequest.setApplication(application);
			DownloadRequestDocument downloadRequestDocument = new DownloadRequestDocument();
			downloadRequestDocument.setId(new Long(id).intValue());
			downloadRequest.setDocument(downloadRequestDocument);
			downloadRequest.setDownloadDocuments(
					new Boolean(true));
			downloadRequest.setAdditionalInfo(
					new Boolean(true));
			downloadRequest.setArchiveInfo(
					new Boolean(true));
			Cws cws = getCwsService();
			DownloadResponse downloadResponse = cws.downloadDocument(
					downloadRequest);
			comprovarResult(downloadResponse.getResult());
			PortafirmesDocument downloadedDocument = new PortafirmesDocument();
			downloadedDocument.setTitol(
					downloadResponse.getDocument().getAttributes().getTitle());
			downloadedDocument.setDescripcio(
					downloadResponse.getDocument().getAttributes().getDescription());
			// estat: 0, 1, 2, 3 (bloquejat, pendent, firmat, rebujat)
			downloadedDocument.setFirmat(
					StateEnum.value2.equals(downloadResponse.getDocument().getAttributes().getState()));
			omplirInformacioArxiuAmbAttachment(
					downloadedDocument,
					cws,
					0);
			return downloadedDocument;
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut descarregar el document del portafirmes",
					ex);
		}
	}

	@Override
	public void delete(
			long id) throws SistemaExternException {
		try {
			DeleteRequest deleteRequest = new DeleteRequest();
			Application application = new Application();
			application.setUser(getUsername());
			application.setPassword(getPassword());
			deleteRequest.setApplication(application);
			DeleteRequestDocument deleteRequestDocument = new DeleteRequestDocument();
			deleteRequestDocument.setId(new Long(id).intValue());
			deleteRequest.setDocuments(new DeleteRequestDocument[] {deleteRequestDocument});
			Cws cws = getCwsService();
			DeleteResponse deleteResponse = cws.deleteDocuments(
					deleteRequest);
			comprovarResult(deleteResponse.getResult());
		} catch (Exception ex) {
			throw new SistemaExternException(
					"No s'ha pogut esborrar el document del portafirmes",
					ex);
		}
	}

	@Override
	public List<PortafirmesDocumentTipus> findDocumentTipus() throws SistemaExternException {
		return null;
	}

	@Override
	public boolean isCustodiaAutomatica() {
		return true;
	}



	private UploadRequestDocument getUploadRequestDocument(
			PortafirmesDocument document,
			Long documentTipus,
			List<PortafirmesDocument> annexos,
			boolean signarAnnexos,
			List<PortafirmesFluxBloc> passos,
			String motiu,
			String remitent,
			PortafirmesPrioritatEnum importancia,
			Date dataLimit) throws Exception {
		UploadRequestDocument uploadRequestDocument = new UploadRequestDocument();
		DocumentAttributes documentAttributes = new DocumentAttributes();
		documentAttributes.setTitle(
				limitarString(document.getTitol(), 100));
		documentAttributes.setDescription(
				limitarString(document.getDescripcio(), 250));
		documentAttributes.setExtension(document.getArxiuExtensio());
		documentAttributes.setType(documentTipus.intValue());
		documentAttributes.setSubject(motiu);
		Sender sender = new Sender();
		sender.setName(
				limitarString(remitent, 50));
		documentAttributes.setSender(sender);
		if (importancia == null) {
			documentAttributes.setImportance(
					ImportanceEnum.normal);
		} else {
			switch (importancia) {
			case BAIXA:
				documentAttributes.setImportance(
						ImportanceEnum.low);
				break;
			case NORMAL:
				documentAttributes.setImportance(
						ImportanceEnum.normal);
				break;
			case ALTA:
				documentAttributes.setImportance(
						ImportanceEnum.high);
				break;
			}
		}
		// 1 - PDF; 2 - P7/CMS/CADES; 3 - XADES;
		documentAttributes.setTypeSign(
				new Integer(getSignaturaTipus()));
		documentAttributes.setIsFileSign(
				new Boolean(document.isFirmat()));
		uploadRequestDocument.setAttributes(documentAttributes);
		Steps steps = new Steps();
		steps.setSignMode(
				SignModeEnum.attached);
		List<Step> stepList = new ArrayList<Step>();
		for (PortafirmesFluxBloc pas: passos) {
			UploadStep step = new UploadStep();
			step.setMinimalSigners(
					new Integer(pas.getMinSignataris()));
			List<Signer> signers = new ArrayList<Signer>();
			for (String signatari: pas.getDestinataris()) {
				Signer signer = new Signer();
				signer.setId(signatari);
				signer.setCheckCert(
						new Boolean(isCheckCert()));
				signers.add(signer);
			}
			step.setSigners(
					signers.toArray(new Signer[signers.size()]));
			stepList.add(step);
		}
		steps.setStep(stepList.toArray(new Step[stepList.size()]));
		uploadRequestDocument.setSteps(steps);
		if (annexos != null) {
			List<Annex> annexes = new ArrayList<Annex>();
			for (PortafirmesDocument annex: annexos) {
				Annex anx = new Annex();
				anx.setDescription(
						limitarString(annex.getDescripcio(), 250));
				anx.setExtension(annex.getArxiuExtensio());
				anx.setTypeSign(
						new Integer(getSignaturaTipus()));
				anx.setIsFileSign(
						new Boolean(annex.isFirmat()));
				Sender anxSender = new Sender();
				anxSender.setName(
						limitarString(remitent, 50));
				anx.setSender(anxSender);
				annexes.add(anx);
			}
			uploadRequestDocument.setAnnexes(
					annexes.toArray(new Annex[annexes.size()]));
		}
		return uploadRequestDocument;
	}

	private Cws getCwsService() throws Exception {
		CwsServiceLocator serviceLocator = new CwsServiceLocator();
		serviceLocator.setCWSEndpointAddress(getServiceUrl());
		Cws cws = serviceLocator.getCWS();
		if (isLogMissatgesActiu()) {
			// TODO log missatges SOAP
		}
		return cws;
	}

	private void addAttachments(
			Cws cws,
			PortafirmesDocument document,
			List<PortafirmesDocument> annexos) {
		DataHandler documentHandler = new DataHandler(
        		new OctetStreamDataSource(
        				document.getArxiuNom(),
        				new OctetStream(document.getArxiuContingut())));
		((CWSSoapBindingStub)cws).addAttachment(documentHandler);
		if (annexos != null) {
			long attachmentBaseName = System.currentTimeMillis();
			int attachmentIndex = 0;
			for (PortafirmesDocument annex: annexos) {
				String attachmentName = "RIP_" + (attachmentBaseName + attachmentIndex++) + ".att";
				DataHandler annexHandler = new DataHandler(
		        		new OctetStreamDataSource(
		        				attachmentName,
		        				new OctetStream(annex.getArxiuContingut())));
				((CWSSoapBindingStub)cws).addAttachment(annexHandler);
			}
		}
	}

	private void omplirInformacioArxiuAmbAttachment(
			PortafirmesDocument document,
			Cws cws,
			int index) throws Exception {
		Object[] attachments = ((CWSSoapBindingStub)cws).getAttachments();
		AttachmentPart attachment = (AttachmentPart)attachments[index];
		DataHandler dataHandler = attachment.getActivationDataHandler();
		//attachment.detachAttachmentFile();
		/*byte[] bytes = new byte[dataHandler.getInputStream().available()];
		dataHandler.getInputStream().read(bytes);
		resposta.add(bytes);*/
		document.setArxiuContingut(
				IOUtils.toByteArray(dataHandler.getInputStream()));
		document.setArxiuNom(dataHandler.getName());
		attachment.dispose();
	}

	private void comprovarResult(Result result) throws SistemaExternException {
		if (result.getCode() != 0 || !"OK".equals(result.getMessage())) {
			throw new SistemaExternException(
					"La resposta del portafirmes no conté un resultat correcte (" +
					"code=" + result.getCode() + ", " +
					"message=" + result.getMessage() + ")");
		}
	}

	private void comprovarExtensioArxiu(
			String extensio) throws SistemaExternException {
		String extensionsPermeses = getExtensionsPermeses();
		if (extensionsPermeses != null && !extensionsPermeses.isEmpty()) {
			String[] permeses = extensionsPermeses.split(",");
			boolean extensioOk = false;
			for (String permesa: permeses) {
				if (extensio.equalsIgnoreCase(permesa.trim())) {
					extensioOk = true;
					break;
				}
			}
			if (!extensioOk) {
				throw new SistemaExternException(
						"L'extensió del document no està permesa (" +
						"permeses=" + extensionsPermeses + ")");
			}
		}
	}

	private String limitarString(String str, int maxChars) {
		if (str == null)
			return null;
		String textFinal = "[...]";
		if (str.length() > maxChars) {
			return str.substring(0, maxChars - textFinal.length()) + textFinal;
		} else {
			return str;
		}
	}

	private String getServiceUrl() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.portafirmes.cws.service.url");
	}
	private String getUsername() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.portafirmes.cws.username");
	}
	private String getPassword() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.portafirmes.cws.password");
	}
	private boolean isCheckCert() {
		return PropertiesHelper.getProperties().getAsBoolean(
				"es.caib.ripea.plugin.portafirmes.cws.check.cert");
	}
	private String getSignaturaTipus() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.portafirmes.cws.signatura.tipus");
	}
	private String getExtensionsPermeses() {
		return PropertiesHelper.getProperties().getProperty(
				"es.caib.ripea.plugin.portafirmes.cws.extensions.permeses");
	}
	private boolean isLogMissatgesActiu() {
		return PropertiesHelper.getProperties().getAsBoolean(
				"es.caib.ripea.plugin.portafirmes.cws.log.actiu");
	}

	/*private class ByteArrayDataSource implements DataSource {
		private byte[] dades;
		private String name;
		private String contentType;
		public ByteArrayDataSource(
				byte[] dades,
				String name,
				String contentType) {
			this.dades = dades;
			this.name = name;
			this.contentType = contentType;
		}
		public OutputStream getOutputStream() throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.write(dades);
			return baos;
		}
		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(dades);
		}
		public String getName() {
			return name;
		}
		public String getContentType() {
			return contentType;
		}
	}*/

}
