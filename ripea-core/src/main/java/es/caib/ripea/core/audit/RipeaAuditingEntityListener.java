/**
 * 
 */
package es.caib.ripea.core.audit;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import es.caib.ripea.core.entity.UsuariEntity;

/**
 * EntityListener per a les auditories per a evitar la configuració
 * via Spring.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class RipeaAuditingEntityListener extends AuditingEntityListener<UsuariEntity> {
	
	boolean auditorAwareConfigurat = false;

	@PrePersist
	public void touchForCreate(Object target) {
		configAuditorAware();
		super.touchForCreate(target);
	}

	@PreUpdate
	public void touchForUpdate(Object target) {
		configAuditorAware();
		super.touchForUpdate(target);
	}

	private void configAuditorAware() {
		if (!auditorAwareConfigurat) {
			AuditingHandler<UsuariEntity> auditingHandler = new AuditingHandler<UsuariEntity>();
			auditingHandler.setAuditorAware(BasicAuditorAware.getInstance());
			setAuditingHandler(auditingHandler);
			auditorAwareConfigurat = true;
		}
	}

}
