/**
 * 
 */
package es.caib.ripea.war.webdav;

import io.milton.http.Auth;
import io.milton.http.Range;
import io.milton.http.Request;
import io.milton.http.Request.Method;
import io.milton.http.XmlWriter;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.ConflictException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.http.exceptions.NotFoundException;
import io.milton.resource.CollectionResource;
import io.milton.resource.FolderResource;
import io.milton.resource.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriUtils;

import es.caib.ripea.core.api.dto.CarpetaDto;
import es.caib.ripea.core.api.dto.CarpetaTipusEnumDto;
import es.caib.ripea.core.api.dto.ContenidorDto;
import es.caib.ripea.core.api.dto.DocumentDto;
import es.caib.ripea.core.api.dto.EntitatDto;
import es.caib.ripea.core.api.dto.ExpedientDto;
import es.caib.ripea.core.api.service.CarpetaService;
import es.caib.ripea.core.api.service.ContenidorService;
import es.caib.ripea.core.api.service.DocumentService;

/**
 * Resource que representa una carpeta.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RipeaFolderResource implements FolderResource {

	private EntitatDto entitat;
	private ContenidorDto contenidor;
	private ServiceHolder serviceHolder;
	private boolean contenidorEntitat;
	private boolean contenidorEscriptori;

	private byte[] htmlContent;



	public RipeaFolderResource(
			EntitatDto entitat,
			ContenidorDto contenidor,
			ServiceHolder serviceHolder) {
		this.entitat = entitat;
		this.contenidor = contenidor;
		this.serviceHolder = serviceHolder;
	}

	@Override
	public String getUniqueId() {
		//logger.debug("[C] getUniqueId " + getIdentificadorPerLog() + ": " + contenidor.getId().toString());
		return contenidor.getId().toString();
	}

	@Override
	public String getName() {
		//logger.debug("[C] getName " + getIdentificadorPerLog() + ": " + contenidor.getNom());
		return contenidor.getNom();
	}

	@Override
	public Object authenticate(String user, String password) {
		//logger.debug("[C] authenticate " + getIdentificadorPerLog() + "");
		return null;
	}

	@Override
	public boolean authorise(Request request, Method method, Auth auth) {
		//logger.debug("[C] authorise " + getIdentificadorPerLog() + ": true");
		return true;
	}

	@Override
	public String getRealm() {
		//logger.debug("[C] getRealm " + getIdentificadorPerLog() + "");
		return "Govern de les Illes Balears";
	}

	@Override
	public Date getModifiedDate() {
		//logger.debug("[C] getModifiedDate " + getIdentificadorPerLog() + ": " + contenidor.getLastModifiedDate());
		return contenidor.getLastModifiedDate();
	}

	@Override
	public String checkRedirect(
			Request request) throws NotAuthorizedException, BadRequestException {
		//logger.debug("[C] checkRedirect " + getIdentificadorPerLog() + ": null");
		return null;
	}

	@Override
	public Date getCreateDate() {
		//logger.debug("[C] getCreateDate " + getIdentificadorPerLog() + ": " + contenidor.getCreatedDate());
		return contenidor.getCreatedDate();
	}

	@Override
	public void sendContent(
			OutputStream out,
			Range range,
			Map<String, String> params,
			String contentType) throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
		logger.debug("[C] sendContent");
		out.write(getHtmlContent());
	}

	@Override
	public Long getMaxAgeSeconds(Auth auth) {
		//logger.debug("[C] getMaxAgeSeconds " + getIdentificadorPerLog() + "");
		return null;
	}

	@Override
	public String getContentType(String accepts) {
		//logger.debug("[C] getContentType " + getIdentificadorPerLog() + ": text/html");
		return "text/html";
	}

	@Override
	public Long getContentLength() {
		Long length = new Long(0);
		/*try {
			length = new Long(getHtmlContent().length);
		} catch (Exception ex) {
			length = new Long(0);
		}*/
		//logger.debug("[C] getContentLength " + getIdentificadorPerLog() + ": " + length);
		return length;
	}

	@Override
	public Resource child(String childName) throws NotAuthorizedException, BadRequestException {
		logger.debug("[C] child " + getIdentificadorPerLog() + "");
		if (contenidor instanceof DocumentDto) {
			DocumentDto document = (DocumentDto)contenidor;
			if (childName.equals(document.getDarreraVersio().getArxiuNom()))
			return new RipeaFileResource(
					entitat,
					document,
					serviceHolder);
		} else if (contenidor.getFills() != null) {
			for (ContenidorDto c: contenidor.getFills()) {
				if (c.getNom().equals(childName)) {
					ContenidorDto contenidor = getContenidorService().getContenidorAmbContingut(
							entitat.getId(),
							c.getId());
					return contenidorToResource(contenidor);
				}
			}
		}
		return null;
	}

	@Override
	public List<? extends Resource> getChildren() throws NotAuthorizedException, BadRequestException {
		List<Resource> children = new ArrayList<Resource>();
		if (contenidor instanceof DocumentDto) {
			DocumentDto document = (DocumentDto)contenidor;
			children.add(
					new RipeaFileResource(
							entitat,
							document,
							serviceHolder));
		} else {
			if (contenidor.getFillsCount() > 0) {
				for (ContenidorDto c: contenidor.getFills()) {
					Resource resource = contenidorToResource(c);
					if (resource != null)
						children.add(resource);
				}
			}
		}
		logger.debug("[C] getChildren " + getIdentificadorPerLog() + ": " + children.size());
		return children;
	}

	@Override
	public Resource createNew(
			String newName,
			InputStream inputStream,
			Long length,
			String contentType) throws IOException, ConflictException, NotAuthorizedException, BadRequestException {
		logger.debug("[C] createNew " + getIdentificadorPerLog() + ": newName=" + newName + ", length=" + length + ", contentType=" + contentType);
		String mimeType = contentType;
		if (mimeType == null) {
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			mimeType = mimeTypesMap.getContentType(newName);
		}
		DocumentDto document = getDocumentService().create(
				contenidor.getEntitat().getId(),
				contenidor.getId(),
				null,
				newName,
				new Date(),
				newName,
				mimeType,
				IOUtils.toByteArray(inputStream));
		return contenidorToResource(document);
	}

	@Override
	public void copyTo(
			CollectionResource toCollection,
			String name) throws NotAuthorizedException, BadRequestException, ConflictException {
		logger.debug("[C] copyTo " + getIdentificadorPerLog() + "");
		getContenidorService().copy(
				contenidor.getEntitat().getId(),
				contenidor.getId(),
				new Long(toCollection.getUniqueId()),
				true);
	}

	@Override
	public void moveTo(
			CollectionResource rDest,
			String name) throws ConflictException, NotAuthorizedException, BadRequestException {
		logger.debug("[C] moveTo " + getIdentificadorPerLog() + "");
		// Si ha canviat el nom el modifica
		if (!name.equals(getName())) {
			getContenidorService().rename(
					contenidor.getEntitat().getId(),
					contenidor.getId(),
					name);
		}
		// Comprova si el destí és aquesta mateixa carpeta abans
		// de moure-lo
		if (rDest.getUniqueId().equals(getUniqueId())) {
			getContenidorService().move(
					contenidor.getEntitat().getId(),
					contenidor.getId(),
					new Long(rDest.getUniqueId()));
		}
	}

	@Override
	public void delete() throws NotAuthorizedException, ConflictException, BadRequestException {
		logger.debug("[C] delete " + getIdentificadorPerLog() + "");
		getContenidorService().deleteReversible(
				contenidor.getEntitat().getId(),
				contenidor.getId());
	}

	@Override
	public CollectionResource createCollection(
			String newName) throws NotAuthorizedException, ConflictException, BadRequestException {
		logger.debug("[C] createCollection " + getIdentificadorPerLog() + "");
		CarpetaDto carpeta = getCarpetaService().create(
				contenidor.getEntitat().getId(),
				contenidor.getId(),
				newName,
				CarpetaTipusEnumDto.ESBORRANY);
		return contenidorToResource(carpeta);
	}

	public boolean isContenidorEntitat() {
		return contenidorEntitat;
	}
	public void setContenidorEntitat(boolean contenidorEntitat) {
		this.contenidorEntitat = contenidorEntitat;
	}
	public boolean isContenidorEscriptori() {
		return contenidorEscriptori;
	}
	public void setContenidorEscriptori(boolean contenidorEscriptori) {
		this.contenidorEscriptori = contenidorEscriptori;
	}



	private FolderResource contenidorToResource(ContenidorDto contenidor) {
		if (contenidor instanceof ExpedientDto) {
			ExpedientDto expedient = (ExpedientDto)contenidor;
			return new RipeaFolderResource(
					entitat,
					expedient,
					serviceHolder); 
		} else if (contenidor instanceof CarpetaDto) {
			CarpetaDto carpeta = (CarpetaDto)contenidor;
			return new RipeaFolderResource(
					entitat,
					carpeta,
					serviceHolder);
		} else if (contenidor instanceof DocumentDto) {
			DocumentDto document = (DocumentDto)contenidor;
			return new RipeaFolderResource(
					entitat,
					document,
					serviceHolder);
		} else {
			return null;
		}
	}
	private String getBasePath() throws IOException {
		StringBuilder path = new StringBuilder();
		path.append("/ripea/webdav");
		if (entitat != null) {
			path.append("/");
			path.append(UriUtils.encodePath(entitat.getNom(), "UTF-8"));
		}
		if (contenidor.getPath() != null) {
			// El primer és l'Escriptori i s'ha d'esborrar
			boolean primer = true;
			for (ContenidorDto pathElement: contenidor.getPath()) {
				if (!primer) {
					path.append("/");
					path.append(UriUtils.encodePath(pathElement.getNom(), "UTF-8"));
				}
				primer = false;
			}
		}
		return path.toString();
	}

	private String getChildUri(
			String basePath,
			Resource resource) throws IOException {
		StringBuilder uri = new StringBuilder();
		uri.append(basePath);
		if (contenidor.getPath() != null && contenidor.getPath().size() > 0) {
			uri.append("/");
			uri.append(UriUtils.encodePath(contenidor.getNom(), "UTF-8"));
		}
		if (resource != null) {
			uri.append("/");
			uri.append(UriUtils.encodePath(resource.getName(), "UTF-8"));
		}
		return uri.toString();
	}

	private byte[] getHtmlContent() throws IOException, NotAuthorizedException, BadRequestException, NotFoundException {
		if (htmlContent == null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String basePath = getBasePath();
			XmlWriter w = new XmlWriter(baos);
			w.open("html");
			w.open("head");
			w.close("head");
			w.open("body");
			w.begin("h1").open().writeText(this.getName()).close();
			if (isContenidorEscriptori())
				w.begin("a").writeAtt("href", basePath + "/..").open().writeText("../").close();
			else
				w.begin("a").writeAtt("href", basePath).open().writeText("../").close();
			w.begin("br").open().close();
			w.open("table");
			for (Resource r : getChildren()) {
				w.open("tr");
				w.open("td");
				if (r instanceof RipeaFolderResource) {
					w.begin("a").writeAtt("href", getChildUri(basePath, r)).open().writeText("[" + r.getName() + "]").close();
				} else {
					w.begin("a").writeAtt("href", getChildUri(basePath, r)).open().writeText(r.getName()).close();
				}
				w.close("td");
				w.begin("td").open().writeText(r.getModifiedDate() + "").close();
				w.close("tr");
			}
			w.close("table");
			w.close("body");
			w.close("html");
			w.flush();
			htmlContent = baos.toByteArray();
		}
		return htmlContent;
	}

	private String getIdentificadorPerLog() {
		return "(id=" + getUniqueId() + ", name=" + getName() + ")";
	}

	private ContenidorService getContenidorService() {
		return serviceHolder.getContenidorService();
	}
	private DocumentService getDocumentService() {
		return serviceHolder.getDocumentService();
	}
	private CarpetaService getCarpetaService() {
		return serviceHolder.getCarpetaService();
	}

	private static final Logger logger = LoggerFactory.getLogger(RipeaFolderResource.class);

}
