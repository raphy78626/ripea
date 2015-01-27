/**
 * 
 */
package es.caib.ripea.war.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador per a la pàgina d'error.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Controller
public class ErrorController {

	//private static final String SESSION_ATRIBUTE_ERROR = "ErrorController.errorObject";

	@RequestMapping(value = "/error")
	public String error(
			HttpServletRequest request,
			Model model) {
		/*request.getSession().setAttribute(
				SESSION_ATRIBUTE_ERROR,
				new ErrorObject(request));
		return "redirect:/errorMostrar";*/
		model.addAttribute(
				"errorObject",
				new ErrorObject(request));
		return "error";
	}
	/*@RequestMapping(value = "/errorMostrar")
	public String error2(
			HttpServletRequest request,
			Model model) {
		model.addAttribute(
				"errorObject",
				request.getSession().getAttribute(SESSION_ATRIBUTE_ERROR));
		request.getSession().removeAttribute(SESSION_ATRIBUTE_ERROR);
		return "error";
	}*/

	public class ErrorObject {
		Integer statusCode;
		Throwable throwable;
		String exceptionMessage;
		String requestUri;
		String message;
		public ErrorObject(HttpServletRequest request) {
			statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
			throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");
			exceptionMessage = getExceptionMessage(throwable, statusCode);
			requestUri = (String)request.getAttribute("javax.servlet.error.request_uri");
			if (requestUri == null) 
				requestUri = "Desconeguda";
			message = 
					"Retornat codi d'error " + statusCode + " "
					+ "per al recurs " + requestUri + " "
					+ "amb el missatge: " + exceptionMessage;
		}
		public Integer getStatusCode() {
			return statusCode;
		}
		public Throwable getThrowable() {
			return throwable;
		}
		public String getExceptionMessage() {
			return exceptionMessage;
		}
		public String getRequestUri() {
			return requestUri;
		}
		public String getMessage() {
			return message;
		}
		public String getStackTrace() {
			return ExceptionUtils.getStackTrace(throwable);
		}
		public String getFullStackTrace() {
			return ExceptionUtils.getFullStackTrace(throwable);
		}
		private String getExceptionMessage(Throwable throwable, Integer statusCode) {
			if (throwable != null) {
				Throwable rootCause = ExceptionUtils.getRootCause(throwable);
				if (rootCause != null)
					return rootCause.getMessage();
				else
					return throwable.getMessage();
			} else {
				HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
				return httpStatus.getReasonPhrase();
			}
		}
	}

}
