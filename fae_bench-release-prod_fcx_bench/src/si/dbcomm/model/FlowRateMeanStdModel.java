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

/**
 * @author Everton Freitas
 *
 */
@Entity
@Table(name = "flowratemeanstdmodel")
public class FlowRateMeanStdModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841400039331658526L;

	@Id
	@SequenceGenerator(name = "flowratemeanstd_seq", sequenceName = "flowratemeanstd_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "flowratemeanstd_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_flowratemeanstd", nullable = false)
	private long id;
	
	@Column(name = "double_avg_temperature", nullable = false)
	private double avgTemperature;
	
	@Column(name = "double_avg_constant_k", nullable = false)
	private double avgConstantK;
	
	@Column(name = "double_avg_reynolds", nullable = false)
	private double avgReynolds;

	@Column(name = "double_std_constant_k", nullable = false)
	private double stdConstantK;
	
	@Column(name = "bool_is_approved", nullable = false)
	private boolean isApproved;
	
	@OneToOne
	@JoinColumn(name = "id_batch", nullable = false)
	private BatchModel batch;
	
	@Column(name = "double_flowrate", nullable = false)
	private double flowRate;
	
	@OneToOne
	@JoinColumn(name = "id_batelada_init", nullable = false)
	private BateladaModel bateladaInit;
	
	@OneToOne
	@JoinColumn(name = "id_batelada_end", nullable = false)
	private BateladaModel bateladaEnd;

	

	public BateladaModel getBateladaInit() {
		return bateladaInit;
	}

	public void setBateladaInit(BateladaModel bateladaInit) {
		this.bateladaInit = bateladaInit;
	}

	public BateladaModel getBateladaEnd() {
		return bateladaEnd;
	}

	public void setBateladaEnd(BateladaModel bateladaEnd) {
		this.bateladaEnd = bateladaEnd;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getAvgTemperature() {
		return avgTemperature;
	}

	public void setAvgTemperature(double avgTemperature) {
		this.avgTemperature = avgTemperature;
	}

	public double getAvgConstantK() {
		return avgConstantK;
	}

	public void setAvgConstantK(double avgConstantK) {
		this.avgConstantK = avgConstantK;
	}

	public double getAvgReynolds() {
		return avgReynolds;
	}

	public void setAvgReynolds(double avgReynolds) {
		this.avgReynolds = avgReynolds;
	}

	public double getStdConstantK() {
		return stdConstantK;
	}

	public void setStdConstantK(double stdConstantK) {
		this.stdConstantK = stdConstantK;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public BatchModel getBatch() {
		return batch;
	}

	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}

	public double getFlowRate() {
		return flowRate;
	}

	public void setFlowRate(double flowRate) {
		this.flowRate = flowRate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
}
