package messages;

import main.Message;

public class CalculationMessage extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7121705981495235355L;
	private double n1;
	private double n2;
	private double solution;
	private CalculationType type;
	
	public CalculationMessage() {
		this(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, CalculationType.NONE);
	}
	
	public CalculationMessage(double n1, double n2, CalculationType type) {
		this(n1, n2, type, Double.NEGATIVE_INFINITY);
	}
	
	public CalculationMessage(double n1, double n2, CalculationType type, double solution) {
		this.n1 = n1;
		this.n2 = n2;
		this.type = type;
		this.solution = solution;
	}
	
	
	
	public enum CalculationType {
		NONE(0),
		ADDITION(1),
		SUBTRACTION(2),
		MULTIPLICATION(3),
		DIVISION(4);
		
		private final int value;
		private CalculationType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
	}

	public double getN1() {
		return n1;
	}

	public double getN2() {
		return n2;
	}

	public double getSolution() {
		return solution;
	}

	public CalculationType getType() {
		return type;
	}
	
	public void setSolution(double value) {
		this.solution = value;
	}
	
	

}
