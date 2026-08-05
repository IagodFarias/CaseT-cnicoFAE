//package util;
//
//import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.geometry.Pos;
//import javafx.scene.control.TextField;
//
//public abstract class MaskTextField {
//
//	/**
//	 * Monta a mascara para Data (dd/MM/yyyy).
//	 *
//	 * @param textField
//	 *            TextField
//	 */
//	public static void dateField(final TextField textField) {
//		maxField(textField, 10);
//
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				if (newValue.intValue() < 11) {
//					String value = textField.getText();
//					value = value.replaceAll("[^0-9]", "");
//					value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2");
//					value = value.replaceFirst("(\\d{2})\\/(\\d{2})(\\d)", "$1/$2/$3");
//					textField.setText(value);
//					positionCaret(textField);
//				}
//			}
//		});
//	}
//
//	/**
//	 * Campo que aceita somente numericos.
//	 *
//	 * @param textField
//	 *            TextField
//	 */
//	public static void numericField(final TextField textField) {
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				if (newValue.intValue() > oldValue.intValue()) {
//					char ch = textField.getText().charAt(oldValue.intValue());
//					if (!(ch >= '0' && ch <= '9')) {
//						textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
//					}
//				}
//			}
//		});
//	}
//
//	/**
//	 * Monta a mascara para Moeda.
//	 *
//	 * @param textField
//	 *            TextField
//	 */
//	public static void monetaryField(final TextField textField) {
//		textField.setAlignment(Pos.CENTER_RIGHT);
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				String value = textField.getText();
//				value = value.replaceAll("[^0-9]", "");
//				value = value.replaceAll("([0-9]{1})([0-9]{14})$", "$1.$2");
//				value = value.replaceAll("([0-9]{1})([0-9]{11})$", "$1.$2");
//				value = value.replaceAll("([0-9]{1})([0-9]{8})$", "$1.$2");
//				value = value.replaceAll("([0-9]{1})([0-9]{5})$", "$1.$2");
//				value = value.replaceAll("([0-9]{1})([0-9]{2})$", "$1,$2");
//				textField.setText(value);
//				positionCaret(textField);
//
//				textField.textProperty().addListener(new ChangeListener<String>() {
//					@Override
//					public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
//						if (newValue.length() > 17)
//							textField.setText(oldValue);
//					}
//				});
//			}
//		});
//
//		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
//				if (!fieldChange) {
//					final int length = textField.getText().length();
//					if (length > 0 && length < 3) {
//						textField.setText(textField.getText() + "00");
//					}
//				}
//			}
//		});
//	}
//
//	// /**
//	// * Monta as mascara para CPF/CNPJ. A mascara eh exibida somente apos o campo perder o foco.
//	// *
//	// * @param textField
//	// * TextField
//	// */
//	// public static void cpfField(final TextField textField) {
//	//
//	// textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
//	//
//	// @Override
//	// public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
//	// String value = textField.getText();
//	// if (!fieldChange) {
//	// if (textField.getText().length() == 11) {
//	// value = value.replaceAll("[^0-9]", "");
//	// value = value.replaceFirst("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})$", "$1.$2.$3-$4");
//	// }
//	// // if (textField.getText().length() == 14) {
//	// // value = value.replaceAll("[^0-9]", "");
//	// // value = value.replaceFirst("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})$",
//	// // "$1.$2.$3/$4-$5");
//	// // }
//	// }
//	// textField.setText(value);
//	// if (textField.getText() != value) {
//	// textField.setText("");
//	// textField.insertText(0, value);
//	// }
//	//
//	// }
//	// });
//	//
//	// maxField(textField, 18);
//	// }
//
//	/**
//	 * Monta a mascara para os campos CPF.
//	 *
//	 * @param textField
//	 *            TextField
//	 */
//	public static void cepField(final TextField textField) {
//		maxField(textField, 10);
//
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//				String value = textField.getText();
//				value = value.replaceAll("[^0-9]", "");
//				value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
//				value = value.replaceFirst("(\\d{2})\\.(\\d{3})(\\d)", "$1.$2-$3");
//				// value = value.replaceFirst("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3-$4");
//				// value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
//				// value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
//				textField.setText(value);
//				positionCaret(textField);
//			}
//		});
//	}
//
//	/**
//	 * Monta a mascara para os campos CPF.
//	 *
//	 * @param textField
//	 *            TextField
//	 */
//	public static void cpfField(final TextField textField) {
//		maxField(textField, 14);
//
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//				String value = textField.getText();
//				value = value.replaceAll("[^0-9]", "");
//				value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
//				value = value.replaceFirst("(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3");
//				value = value.replaceFirst("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3-$4");
//				// value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
//				// value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
//				textField.setText(value);
//				positionCaret(textField);
//			}
//		});
//	}
//
//	/**
//	 * Monta a mascara para os campos CNPJ.
//	 *
//	 * @param textField
//	 *            TextField
//	 */
//	public static void cnpjField(final TextField textField) {
//		maxField(textField, 18);
//
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//				String value = textField.getText();
//				value = value.replaceAll("[^0-9]", "");
//				value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
//				value = value.replaceFirst("(\\d{2})\\.(\\d{3})(\\d)", "$1.$2.$3");
//				value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
//				value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
//				textField.setText(value);
//				positionCaret(textField);
//			}
//		});
//
//	}
//
//	public static void cellPhoneField(final TextField textField) {
//		maxField(textField, 16);
//
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//				String value = textField.getText();
//				value = value.replaceAll("[^0-9]", "");
//				value = value.replaceFirst("(\\d{2})(\\d)", "($1) $2");
//				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{1})(\\d)", "($1) $2 $3");
//				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{1})\\ (\\d{4})(\\d)", "($1) $2 $3-$4");
//				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{1})\\ (\\d{4})\\ (\\d{4})", "($1) $2 $3-$4");
//				textField.setText(value);
//				positionCaret(textField);
//			}
//		});
//	}
//
//	public static void phoneField(final TextField textField) {
//		maxField(textField, 14);
//
//		textField.lengthProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//				String value = textField.getText();
//				value = value.replaceAll("[^0-9]", "");
//				value = value.replaceFirst("(\\d{2})(\\d)", "($1) $2");
//				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{4})(\\d)", "($1) $2-$3");
//				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{4})\\-(\\d{4})", "($1) $2-$3");
//				textField.setText(value);
//				positionCaret(textField);
//			}
//		});
//	}
//
//	// public static void phoneField(final TextField textField) {
//	// maxField(textField, 15);
//	//
//	// textField.lengthProperty().addListener(new ChangeListener<Number>() {
//	// @Override
//	// public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//	//
//	// MaskFormatter formatter = new MaskFormatter("###-####");
//	// formatter.setPlaceholderCharacter('_');
//	// formatter.setValidCharacters("0123456789");
//	// formatter.getDisplayValue(textField.getText(), "123");
//	// }
//	// });
//	// }
//
//	/**
//	 * Devido ao incremento dos caracteres das mascaras eh necessario que o cursor sempre se posicione no final da
//	 * string.
//	 *
//	 * @param textField
//	 *            TextField
//	 */
//	private static void positionCaret(final TextField textField) {
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				// Posiciona o cursor sempre a direita.
//				textField.positionCaret(textField.getText().length());
//			}
//		});
//	}
//
//	/**
//	 * @param textField
//	 *            TextField.
//	 * @param length
//	 *            Tamanho do campo.
//	 */
//	private static void maxField(final TextField textField, final Integer length) {
//		textField.textProperty().addListener(new ChangeListener<String>() {
//			@Override
//			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
//				if (newValue.length() > length)
//					textField.setText(oldValue);
//			}
//		});
//	}
//
//}

package util;

import java.text.NumberFormat;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;

public abstract class MaskTextField {

	// /**
	// *
	// */
	// public static void ipAddressField(final TextField textField) {
	// maxField(textField, 15);
	//
	// textField.lengthProperty().addListener(new ChangeListener<Number>() {
	// @Override
	// public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
	// String value = textField.getText();
	// value = value.replaceAll("[^0-9]", "");
	// value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
	// value = value.replaceFirst("(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3");
	// value = value.replaceFirst("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3.$4");
	// // value = value.replaceFirst("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3-$4");
	// // value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
	// // value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
	// textField.setText(value);
	// positionCaret(textField);
	// }
	// });
	// }
	
//	/**
//	 * 
//	 */
//	public static boolean validadeIpAddress(final TextField textField) {
//
//		boolean result = false;
//		String ipAddress = textField.getText();
//		String[] arrayStr = ipAddress.split("\\.");
//		if (arrayStr.length == 4) {
//			ipAddress = "" + Integer.valueOf(arrayStr[0]) + "." + Integer.valueOf(arrayStr[1]) + "." + Integer.valueOf(arrayStr[2]) + "." + Integer.valueOf(arrayStr[3]);
//			textField.setText(ipAddress);
//			result = true;
//		} else {
//			result = false;
//		}
//
//		return result;
//	}

	/**
	 * 
	 */
	public static void ipAddressField(final TextField textField) {
		maxField(textField, 15);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9.]", "");
				value = value.replaceAll("[.][.]", "\\.");
				value = value.replaceAll("([0-9]{1,3})[.]([0-9]{1,3})[.]([0-9]{1,3})[.]([0-9]{1,3})[.]", "$1.$2.$3.$4");
				value = value.replaceAll("([0-9]{1,3})[.]([0-9]{1,3})[.]([0-9]{1,3})[.]([0-9]{3})[0-9]", "$1.$2.$3.$4");
				value = value.replaceAll("(\\d{3})(\\d)", "$1.$2");
				textField.setText(value);
				positionCaret(textField);
			}
		});
	}

	/**
	 * Monta a mascara para Data (dd/MM/yyyy)
	 *
	 * @param textField
	 *            TextField
	 */
	public static void dateField(final TextField textField) {
		maxField(textField, 10);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() < 11) {
					String value = textField.getText();
					value = value.replaceAll("[^0-9]", "");
					value = value.replaceFirst("(\\d{2})(\\d)", "$1/$2");
					value = value.replaceFirst("(\\d{2})\\/(\\d{2})(\\d)", "$1/$2/$3");
					textField.setText(value);
					positionCaret(textField);
				}
			}
		});
	}

	/**
	 * Campo que aceita somente numericos.
	 *
	 * @param textField
	 *            TextField
	 */
	public static void numericField(final TextField textField) {
		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					char ch = textField.getText().charAt(oldValue.intValue());
					if (!(ch >= '0' && ch <= '9')) {
						textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					}
				}
			}
		});
	}

	/**
	 * 
	 */
	public static void numericField(final TextField textField, final int maxLength) {
		maxField(textField, maxLength);
		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					char ch = textField.getText().charAt(oldValue.intValue());
					if (!(ch >= '0' && ch <= '9')) {
						textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					}
				}
				// if (textField.getText().length() > maxLength) {
				// String s = textField.getText().substring(0, maxLength);
				// textField.setText(s);
				// }
			}
		});
	}

	/**
	 * 
	 */
	public static void decimalField(final TextField textField, final int maxLength, final int numDecimal) {
		// int numDecimal = 1;
		maxField(textField, maxLength);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

				if (newValue.intValue() > oldValue.intValue()) {
					String text = textField.getText();
					try {
						String replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance().getCurrency().getSymbol());
						String cleanString = text.toString().replaceAll(replaceable, "");

						if (cleanString.length() == 1) {
							cleanString = "000" + cleanString;
						} else if (cleanString.length() == 2) {
							cleanString = "00" + cleanString;
						} else if (cleanString.length() == 3) {
							cleanString = "0" + cleanString;
						} else {
							if (cleanString.substring(0, 1).equals("0")) {
								cleanString = cleanString.substring(1);
							}
						}

						String reverseValue = new StringBuilder(cleanString).reverse().toString();
						StringBuilder finalValue = new StringBuilder();
						for (int i = 1; i <= reverseValue.length(); i++) {
							char val = reverseValue.charAt(i - 1);
							finalValue.append(val);

							//
							if (i == numDecimal && i != reverseValue.length() && i > 0) {
								finalValue.append(".");
							}
							//
							else if (i == (numDecimal + 3) && i != reverseValue.length() && i > 0) {
								finalValue.append(",");
							}
							//
							else if (i == (numDecimal + 6) && i != reverseValue.length() && i > 0) {
								finalValue.append(",");
							}
							//
							else if (i == (numDecimal + 9) && i != reverseValue.length() && i > 0) {
								finalValue.append(",");
							}
							//
							else if (i == (numDecimal + 12) && i != reverseValue.length() && i > 0) {
								finalValue.append(",");
							}
							//
							else if (i == (numDecimal + 15) && i != reverseValue.length() && i > 0) {
								finalValue.append(".");
							}
						}
						textField.setText(finalValue.reverse().toString());
						// inputValue.setSelection(finalValue.length());

						// inputValue.addTextChangedListener(this);

					} catch (Exception e) {
						// // Do nothing since not a number
						// }finally {
						// inputValue.addTextChangedListener(this);
					}
				}
			}
		});
	}

	/**
	 * 
	 */
	public static void range(final TextField textField, final double min, final double max) {
		// maxField(textField, maxLength);
		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (newValue.intValue() > oldValue.intValue()) {
					double value = Double.valueOf(textField.getText());
					if ((value < min) || (value > max)) {
						textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					}
					// char ch = textField.getText().charAt(oldValue.intValue());
					// if (!(ch >= '0' && ch <= '9')) {
					// textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
					// }
				}
			}
		});
	}

	/**
	 * Monta a mascara para Moeda.
	 *
	 * @param textField
	 *            TextField
	 */
	public static void monetaryField(final TextField textField) {
		textField.setAlignment(Pos.CENTER_RIGHT);
		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceAll("([0-9]{1})([0-9]{14})$", "$1.$2");
				value = value.replaceAll("([0-9]{1})([0-9]{11})$", "$1.$2");
				value = value.replaceAll("([0-9]{1})([0-9]{8})$", "$1.$2");
				value = value.replaceAll("([0-9]{1})([0-9]{5})$", "$1.$2");
				value = value.replaceAll("([0-9]{1})([0-9]{2})$", "$1,$2");
				textField.setText(value);
				positionCaret(textField);

				textField.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
						if (newValue.length() > 17)
							textField.setText(oldValue);
					}
				});
			}
		});

		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
				if (!fieldChange) {
					final int length = textField.getText().length();
					if (length > 0 && length < 3) {
						textField.setText(textField.getText() + "00");
					}
				}
			}
		});
	}

	// /**
	// * Monta as mascara para CPF/CNPJ. A mascara eh exibida somente apos o campo perder o foco.
	// *
	// * @param textField
	// * TextField
	// */
	// public static void cpfField(final TextField textField) {
	//
	// textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	//
	// @Override
	// public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean fieldChange) {
	// String value = textField.getText();
	// if (!fieldChange) {
	// if (textField.getText().length() == 11) {
	// value = value.replaceAll("[^0-9]", "");
	// value = value.replaceFirst("([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})$", "$1.$2.$3-$4");
	// }
	// // if (textField.getText().length() == 14) {
	// // value = value.replaceAll("[^0-9]", "");
	// // value = value.replaceFirst("([0-9]{2})([0-9]{3})([0-9]{3})([0-9]{4})([0-9]{2})$",
	// // "$1.$2.$3/$4-$5");
	// // }
	// }
	// textField.setText(value);
	// if (textField.getText() != value) {
	// textField.setText("");
	// textField.insertText(0, value);
	// }
	//
	// }
	// });
	//
	// maxField(textField, 18);
	// }

	/**
	 * Monta a mascara para os campos CPF.
	 *
	 * @param textField
	 *            TextField
	 */
	public static void cepField(final TextField textField) {
		maxField(textField, 10);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
				value = value.replaceFirst("(\\d{2})\\.(\\d{3})(\\d)", "$1.$2-$3");
				// value = value.replaceFirst("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3-$4");
				// value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
				// value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
				textField.setText(value);
				positionCaret(textField);
			}
		});
	}

	/**
	 * Monta a mascara para os campos CPF.
	 *
	 * @param textField
	 *            TextField
	 */
	public static void cpfField(final TextField textField) {
		maxField(textField, 14);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
				value = value.replaceFirst("(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3");
				value = value.replaceFirst("(\\d{3})\\.(\\d{3})\\.(\\d{3})(\\d)", "$1.$2.$3-$4");
				// value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
				// value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
				textField.setText(value);
				positionCaret(textField);
			}
		});
	}

	/**
	 * Monta a mascara para os campos CNPJ.
	 *
	 * @param textField
	 *            TextField
	 */
	public static void cnpjField(final TextField textField) {
		maxField(textField, 18);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceFirst("(\\d{2})(\\d)", "$1.$2");
				value = value.replaceFirst("(\\d{2})\\.(\\d{3})(\\d)", "$1.$2.$3");
				value = value.replaceFirst("\\.(\\d{3})(\\d)", ".$1/$2");
				value = value.replaceFirst("(\\d{4})(\\d)", "$1-$2");
				textField.setText(value);
				positionCaret(textField);
			}
		});

	}

	public static void cellPhoneField(final TextField textField) {
		maxField(textField, 16);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceFirst("(\\d{2})(\\d)", "($1) $2");
				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{1})(\\d)", "($1) $2 $3");
				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{1})\\ (\\d{4})(\\d)", "($1) $2 $3-$4");
				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{1})\\ (\\d{4})\\ (\\d{4})", "($1) $2 $3-$4");
				textField.setText(value);
				positionCaret(textField);
			}
		});
	}

	public static void phoneField(final TextField textField) {
		maxField(textField, 14);

		textField.lengthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
				String value = textField.getText();
				value = value.replaceAll("[^0-9]", "");
				value = value.replaceFirst("(\\d{2})(\\d)", "($1) $2");
				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{4})(\\d)", "($1) $2-$3");
				value = value.replaceFirst("\\((\\d{2})\\)\\ (\\d{4})\\-(\\d{4})", "($1) $2-$3");
				textField.setText(value);
				positionCaret(textField);
			}
		});
	}

	// public static void phoneField(final TextField textField) {
	// maxField(textField, 15);
	//
	// textField.lengthProperty().addListener(new ChangeListener<Number>() {
	// @Override
	// public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
	//
	// MaskFormatter formatter = new MaskFormatter("###-####");
	// formatter.setPlaceholderCharacter('_');
	// formatter.setValidCharacters("0123456789");
	// formatter.getDisplayValue(textField.getText(), "123");
	// }
	// });
	// }
	
	/**
	 * @param textField
	 *            TextField.
	 * @param length
	 *            Tamanho do campo.
	 */
	public static void maxField(final TextField textField, final Integer length) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
				if (newValue.length() > length)
					textField.setText(oldValue);
			}
		});
	}

	/**
	 * Devido ao incremento dos caracteres das mascaras eh necessario que o cursor sempre se posicione no final da string.
	 *
	 * @param textField
	 *            TextField
	 */
	private static void positionCaret(final TextField textField) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Posiciona o cursor sempre a direita.
				textField.positionCaret(textField.getText().length());
			}
		});
	}

}