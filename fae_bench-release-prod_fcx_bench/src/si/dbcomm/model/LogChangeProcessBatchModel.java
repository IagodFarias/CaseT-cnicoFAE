package si.dbcomm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import si.dbcomm.enumerations.CalibrationTypeEnum;


/**
 * @author Everton Freitas
 *
 */
@Entity
@Table(name = "logchangeprocessbatchmodel")
public class LogChangeProcessBatchModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1952465334368687760L;

	/**
	 * 
	 */
	
	@Id
	@SequenceGenerator(name = "logchangeprocessbatch_seq", sequenceName = "logchangeprocessbatch_seq", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "logchangeprocessbatch_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "id_logchangeprocessbatch", nullable = false)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "id_batelada_init", nullable = true)
	private BateladaModel bateladaInit;
	
	@OneToOne
	@JoinColumn(name = "id_batelada_end", nullable = true)
	private BateladaModel bateladaEnd;
	
	@OneToOne
	@JoinColumn(name = "id_batch", nullable = false)
	private BatchModel batch;
	
	@Column(name = "bool_usebatelada", nullable = false)
	private boolean useBatelada;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "enum_calibration_type", nullable = true)
	private CalibrationTypeEnum calibrationTypeEnum = CalibrationTypeEnum.FULL_PROD;
	

	public CalibrationTypeEnum getCalibrationTypeEnum() {
		return calibrationTypeEnum;
	}

	public void setCalibrationTypeEnum(CalibrationTypeEnum calibrationTypeEnum) {
		this.calibrationTypeEnum = calibrationTypeEnum;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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

	public BatchModel getBatch() {
		return batch;
	}

	public void setBatch(BatchModel batch) {
		this.batch = batch;
	}

	public boolean isUseBatelada() {
		return useBatelada;
	}

	public void setUseBatelada(boolean useBatelada) {
		this.useBatelada = useBatelada;
	}
	
	

}
