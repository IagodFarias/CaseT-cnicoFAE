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
@Table(name = "distancepointsmodel")
public class DistancePointsModel implements Serializable{

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 6276437527792916439L;
	
	@Id
	@SequenceGenerator(name = "distancepoint_seq", sequenceName = "distancepoint_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "distancepoint_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_distancepoint", nullable=false)
	private Long id;
	
	@Column(name = "double_flowratefrom", nullable=false)
	private Double flowRateFrom;
	
	@Column(name = "double_flowrateto", nullable=false)
	private Double flowRateTo;
	
	@Column(name = "double_xdistance", nullable=false)
	private Double xDistance;
	
	@Column(name = "double_ydistance", nullable=false)
	private Double yDistance;
	
	@OneToOne
	@JoinColumn(name = "id_batch", nullable=false)
	private BatchModel batch;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getFlowRateFrom() {
		return flowRateFrom;
	}

	public void setFlowRateFrom(Double flowRateFrom) {
		this.flowRateFrom = flowRateFrom;
	}

	public Double getFlowRateTo() {
		return flowRateTo;
	}

	public void setFlowRateTo(Double flowRateTo) {
		this.flowRateTo = flowRateTo;
	}

	public Double getxDistance() {
		return xDistance;
	}

	public void setxDistance(Double xDistance) {
		this.xDistance = xDistance;
	}

	public Double getyDistance() {
		return yDistance;
	}

	public void setyDistance(Double yDistance) {
		this.yDistance = yDistance;
	}

	public BatchModel getBatch() {
		return batch;
	}

	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}

}
