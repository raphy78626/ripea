package es.caib.ripea.core.helper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.event.EventCartridge;
import org.odftoolkit.odfdom.converter.pdf.PdfConverter;
import org.odftoolkit.odfdom.converter.pdf.PdfOptions;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import com.lowagie.text.pdf.PdfWriter;

import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.itext.extension.IPdfWriterConfiguration;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

@Component
public class PlantillaHelper {

	public static byte[] generate(
			byte[] plantilla,
			Map<String, Object> model,
			boolean useVelocity) throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		if (useVelocity) {
			generarAmbPlantillaVelocity(plantilla, model, out);
		} else {
			generarAmbPlantillaFreemarker(plantilla, model, out);
		}

		byte[] content = out.toByteArray();
		out.close();

		return content;
	}
	
	public static byte[] generatePDF(
			byte[] plantilla,
			boolean pdfa, 
			Map<String, Object> model,
			boolean useVelocity) throws Exception {

		byte[] inContent = generate(
				plantilla, 
				model,
				useVelocity);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(inContent);

		OdfTextDocument document = OdfTextDocument.loadDocument(in);
		PdfOptions options = PdfOptions.create();
		if (pdfa) {
			options.setConfiguration(new IPdfWriterConfiguration() {
				public void configure(PdfWriter writer) {
					writer.setPDFXConformance(PdfWriter.PDFA1A);
				}
			});
		}
		PdfConverter.getInstance().convert(document, out, options);

		byte[] content = out.toByteArray();
		out.close();

		return content;
	}
	
	private static void generarAmbPlantillaFreemarker(byte[] content,
			Map<String, Object> model, OutputStream out) throws Exception {
		InputStream in = new ByteArrayInputStream(content);
		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
				TemplateEngineKind.Freemarker);

		report.process(model, out);
	}
	
	private static void generarAmbPlantillaVelocity(byte[] content,
			Map<String, Object> model, OutputStream out) throws Exception {

		InputStream in = new ByteArrayInputStream(content);
		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);

		// Create fields metadata to manage lazy loop (#foreach velocity)
		if (model != null && model.size() > 0) {
			for (Map.Entry<String, Object> entry : model.entrySet()) {
				if (entry.getValue() instanceof FieldsMetadata) {
					report.setFieldsMetadata((FieldsMetadata) entry.getValue());
				}
			}
		}

		IContext context = report.createContext();
		// Aqui podem posar variables i funcionalitats adicionals
		context.put("defaultDateFormat", new SimpleDateFormat("dd/MM/yyyy"));
		// Format per a Float
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
		context.put("decimalFormat", decimalFormat);
		
		context.putMap(model);

		EventCartridge eventCartridge = new EventCartridge();
//		eventCartridge.addEventHandler(new NullValueInsertionEventHandler());
		eventCartridge.attachToContext((VelocityContext) context);
		report.process(context, out);
	}
	
}
