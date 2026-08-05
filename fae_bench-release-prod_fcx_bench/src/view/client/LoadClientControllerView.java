//=============================================================================
/*!
*    Fae Tecnologia
*    All rights reserved
*
*    Coding standards: v1.1
* 
*    @brief <Brief Description>
*    @file LoadFlowRateControllerView.java
*    @author osmenio
*    @date 13 de mar de 2017
*    @details <Detailed Description>
* 
*/
//=============================================================================
package view.client;

import java.io.IOException;
import java.util.Optional;

import enumerations.SceneTypeEnum;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import si.dbcomm.exceptions.CrudDatabaseException;
import si.dbcomm.model.ClientModel;
import si.dbcomm.service.ClientService;
import view.controller.SceneFactory;

/**
 * @author marcos
 *
 */
public class LoadClientControllerView {
	ClientService clientService;

	@FXML
	private Button btnCancel;

	@FXML
	private TableView<ClientModel> clientTable;

	@FXML
	private TableColumn<ClientModel, String> columnFantasyName;

	@FXML
	private TableColumn<ClientModel, String> columnSocialName;

	@FXML
	private TableColumn<ClientModel, String> columnStateRegistration;

	@FXML
	private TableColumn<ClientModel, String> columnEmail;

	@FXML
	private TableColumn<ClientModel, String> columnCNPJ;

	@FXML
	private TableColumn<ClientModel, String> columnAddress;

	@FXML
	private TableColumn<ClientModel, String> columnCEP;

	@FXML
	private TableColumn<ClientModel, String> columnBusinessContact;

	@FXML
	private TableColumn<ClientModel, String> columnTel;

	@FXML
	private TableColumn<ClientModel, String> columnCel;
	
	@SuppressWarnings("rawtypes")
	private TableColumn columnDeleteButton;

	private ObservableList<ClientModel> clientData;

	/**
	 * Inicializa a classe controller. Este método é chamado automaticamente após o arquivo fxml ter sido carregado.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@FXML
	private void initialize() {

		columnFantasyName.setCellValueFactory(new PropertyValueFactory<>("nomeFantasia"));
		columnSocialName.setCellValueFactory(new PropertyValueFactory<>("razaoSocial"));
		columnStateRegistration.setCellValueFactory(new PropertyValueFactory<>("inscricaoEstadual"));
		columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		columnCNPJ.setCellValueFactory(new PropertyValueFactory<>("cnpj"));
		columnCEP.setCellValueFactory(new PropertyValueFactory<>("cep"));
		columnBusinessContact.setCellValueFactory(new PropertyValueFactory<>("contato"));
		columnTel.setCellValueFactory(new PropertyValueFactory<>("telefone"));
		columnCel.setCellValueFactory(new PropertyValueFactory<>("celular"));

		columnFantasyName.setStyle("-fx-alignment: CENTER;");
		columnSocialName.setStyle("-fx-alignment: CENTER;");
		columnStateRegistration.setStyle("-fx-alignment: CENTER;");
		columnEmail.setStyle("-fx-alignment: CENTER;");
		columnCNPJ.setStyle("-fx-alignment: CENTER;");
		columnCEP.setStyle("-fx-alignment: CENTER;");
		columnBusinessContact.setStyle("-fx-alignment: CENTER;");
		columnTel.setStyle("-fx-alignment: CENTER;");
		columnCel.setStyle("-fx-alignment: CENTER;");

		columnAddress.setStyle("-fx-alignment: CENTER;");
		columnAddress.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ClientModel, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TableColumn.CellDataFeatures<ClientModel, String> p) {
				ObservableValue<String> objectWrapper = null;
				// if (p.getValue().getRamal() != null) {
				// objectWrapper = new ReadOnlyObjectWrapper<>(p.getValue().getRamal().getTag());
				// }
				// return objectWrapper;

				ClientModel client = p.getValue();
				String address = "" + client.getLogradouro() + ", " + client.getBairro() + " | " + client.getCidade() + "-" + client.getUf();
				objectWrapper = new ReadOnlyObjectWrapper<>(address);
				return objectWrapper;
			}
		});

		// -------------------------------------
		// Delete button
		columnDeleteButton = new TableColumn("Excluir");
		columnDeleteButton.setStyle("-fx-alignment: CENTER;");
		columnDeleteButton.setCellValueFactory(new PropertyValueFactory<>("Button"));
		columnDeleteButton.setCellFactory(cellFactoryExcluir);
		clientTable.getColumns().add(columnDeleteButton);

		// -----------------------------------------
		clientService = new ClientService();
		clientData = FXCollections.observableArrayList(clientService.findAll());
		clientTable.setItems(clientData);
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionNewButton(ActionEvent event) {
		inflateNewClient(null);
	}

	/**
	 * 
	 * 
	 */
	@FXML
	private void onActionCancelButton(ActionEvent event) {
		// get a handle to the stage
		Stage stage = (Stage) btnCancel.getScene().getWindow();
		stage.close();
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@FXML
	private void onTableMouseClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() == 2) {
				Node node = ((Node) event.getTarget()).getParent();
				TableRow row;
				if (node instanceof TableRow) {
					row = (TableRow) node;
				} else {
					row = (TableRow) node.getParent();
				}
				//
				inflateNewClient((ClientModel) row.getItem());
			}
		}
	}

	/**
	 * 
	 * 
	 */
	private void inflateNewClient(ClientModel client) {

		try {
			SceneFactory sceneFactory = new SceneFactory(SceneTypeEnum.NEW_CLIENT);
			sceneFactory.setTitle("Atualização de Vazão");
			NewClientControllerView childController = sceneFactory.getController();
			if (client != null) {
				childController.setBatch(client);
			}
			childController.setCallback(value -> {
				refreshClientTable();
			});
			sceneFactory.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 */
	private void inflateClientDeleteDialog(ClientModel batch) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Deletar cliente");
		alert.setHeaderText("Cliente: " + batch.getNomeFantasia());
		alert.setContentText("Tem certeza que deseja excluir este cliente?");
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			try {
				// Delete selected batch
				clientService.delete(batch);
				refreshClientTable();
			} catch (CrudDatabaseException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 
	 */
	private void refreshClientTable() {
		clientData = FXCollections.observableArrayList(clientService.findAll());
		clientTable.setItems(clientData);
	}

	/**
	 * 
	 * 
	 */
	private Callback<TableColumn<Object, String>, TableCell<Object, String>> cellFactoryExcluir = new Callback<TableColumn<Object, String>, TableCell<Object, String>>() {
		@Override
		public TableCell<Object, String> call(final TableColumn<Object, String> param) {
			final TableCell<Object, String> cell = new TableCell<Object, String>() {

				final Button btn = new Button("Excluir");

				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
						setText(null);
					} else {

						btn.setOnAction((ActionEvent event) -> {
							// Object objeto = getTableView().getItems().get(getIndex());
							ClientModel clientModel = (ClientModel) getTableView().getItems().get(getIndex());
							inflateClientDeleteDialog(clientModel);
						});
						setGraphic(btn);
						setText(null);
					}
				}
			};
			return cell;
		}
	};
}
