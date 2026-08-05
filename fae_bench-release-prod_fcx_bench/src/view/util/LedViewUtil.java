//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file LedViewUtil.java
*    @author marcos
*    @date 2 de jan de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author marcos
 *
 */
public class LedViewUtil {
	
//	static final Image IMAGE_LED_AMARELO = new Image("File:../../rsrc/images/ledAmarelo.png");
//	static final Image IMAGE_LED_PRETO = new Image("File:../../rsrc/images/ledOff.png");
//	static final Image IMAGE_LED_VERMELHO = new Image("File:../../rsrc/images/ledVermelho.png");
//	static final Image IMAGE_LED_VERDE = new Image("File:../../rsrc/images/ledVerde.png");
	
	static final Image IMAGE_LED_AMARELO = new Image("/view/images/ledAmarelo.png");
	
	static final Image IMAGE_LED_PRETO = new Image("/view/images/ledOff.png");
	
	static final Image IMAGE_LED_VERMELHO = new Image("/view/images/ledVermelho.png");
	
	static final Image IMAGE_LED_VERDE = new Image("/view/images/ledVerde.png");
	
	ExecutorService threadpool;
	
	private ImageView imageView;
	
	private BooleanProperty statusProperty = new SimpleBooleanProperty();

	{
		statusProperty.addListener(new ChangeListener<Boolean>() {
			@Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				toggleLed();
            }
		});
	}
	
	/**
	 * Creates a object for class LedViewUtil.java
	 */
	public LedViewUtil(ImageView imageView) {
		this.imageView = imageView;
		this.imageView.setImage(IMAGE_LED_PRETO);
	}
	
	public boolean isStatus(){
		return statusProperty.get();
	}
	public void setStatus(boolean v){
		statusProperty.setValue(v);
	}
	
	public void turnLedRed(){
		if(this.imageView != null){
			this.imageView.setImage(IMAGE_LED_VERMELHO);
		}else{
			System.err.println("ERROR: image view is null");
		}
	}
		
	public void turnLedGreen(){
		if(this.imageView != null){
			this.imageView.setImage(IMAGE_LED_VERDE);
		}else{
			System.err.println("ERROR: image view is null");
		}
	}
	
	public void turnLedOff(){
		if(this.imageView != null){
			this.imageView.setImage(IMAGE_LED_PRETO);
		}else{
			System.err.println("ERROR: image view is null");
		}
	}
	
	public void toggleLed(){
		if(this.imageView != null){
//			if(this.imageView.getImage() != OFF_LED){
				if(statusProperty.get()){
					// if the led is GREEN, set it to red
					this.imageView.setImage(IMAGE_LED_VERDE);
				}else{
					this.imageView.setImage(IMAGE_LED_VERMELHO);
				}
//			}
		}else{
			System.err.println("ERROR: image view is null");
		}
	}
	
	public void startAlternateLed(Image imageToAlternate, long msecOn, long msecOff){
		threadpool = Executors.newFixedThreadPool(1);
		threadpool.submit(() -> {
			try {
				while(true){
					
					this.imageView.setImage(imageToAlternate);
					Thread.sleep(msecOn);
					this.imageView.setImage(IMAGE_LED_PRETO);
					Thread.sleep(msecOff);
				}
				
			} catch (InterruptedException e) {
				this.imageView.setImage(IMAGE_LED_PRETO);
			}
		});
	}
	
	public void stopAlternateLed(){
		if(threadpool != null){
			if(!threadpool.isShutdown()){
				threadpool.shutdownNow();
				this.imageView.setImage(IMAGE_LED_PRETO);
			}
		}
	}
	
	public void alternateRedAndOff(){
		startAlternateLed(IMAGE_LED_VERMELHO, 250, 250);
	}
	
}
