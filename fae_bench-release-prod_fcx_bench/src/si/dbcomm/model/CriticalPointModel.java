package si.dbcomm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "criticalpointmodel")
public class CriticalPointModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7143441069744732377L;

	@Id
	@SequenceGenerator(name = "criticalpoint_seq", sequenceName = "criticalpoint_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "criticalpoint_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_criticalpoint", nullable = false)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_batch", nullable = false)
	private BatchModel batch;
	
	@OneToOne
	@JoinColumn(name = "id_flowrate", nullable = false)
	private FlowRateModel flowRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BatchModel getBatch() {
		return batch;
	}

	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}

	public FlowRateModel getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(FlowRateModel flowRate) {
		this.flowRate = flowRate;
	}
	
	
	
}
