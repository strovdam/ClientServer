package a4;

import java.io.Serializable;

public class ConnectFourField implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3073236141850621872L;
	private byte[][] field = new byte[6][7];
	
	public void set(int x, int y, int playerIndex) {
		field[y][x] = (byte)playerIndex;
	}
	
	public void fall(int x, int playerIndex) {
		if(x < 0 || x >= field[0].length)
			return;
		for(int i = field.length - 1; i >= 0; i--) {
			if(field[i][x] == 0) {
				set(x, i, playerIndex);
				return;
			}
		}
	}
	
	public byte checkWinner() {
		byte checking = 0;
		int checkLength = 0;
		for(int i = 0; i < field.length; i++) {
			checking = 0;
			checkLength = 0;
			for(int g = 0; g < field[i].length; g++) {
				if(field[i][g] == checking) {
					checkLength++;
				} else {
					checkLength = 1;
				}
				checking = field[i][g];
				if(checkLength == 4 && checking != 0)
					return checking;
			}
		}
		int g = 0;
		for(int i = 0; i < field[g].length; i++) {
			checking = 0;
			checkLength = 0;
			for(g = 0; g < field.length; g++) {
				if(field[g][i] == checking) {
					checkLength++;
				} else {
					checkLength = 1;
				}
				checking = field[g][i];
				if(checkLength == 4 && checking != 0)
					return checking;
			}
			g = 0;
		}
		for(int i = 0; i < field.length; i++) {
			checking = 0;
			checkLength = 0;
			for(g = 0; g < field[i].length; g++) {
				if(field[i][g] != 0 && i + 3 < field.length) {
					if(g + 3 < field[i].length) {
						checking = field[i][g];
						for(int h = 1; h < 4; h++) {
							if(field[i+h][g+h] == checking) {
								checkLength++;
								if(checkLength == 3) {
									return checking;
								}
							} else {
								checking = 0;
								checkLength = 0;
								break;
							}
						}
					}
					if(g - 3 >= 0) {
						checking = field[i][g];
						for(int h = 1; h < 4; h++) {
							if(field[i+h][g-h] == checking) {
								checkLength++;
								if(checkLength == 3) {
									return checking;
								}
							} else {
								checking = 0;
								checkLength = 0;
								break;
							}
						}
					}
				} else {
					checkLength = 0;
				}
			}
		}
		return 0;
	}
	
	public void print() {
		for(int i = 0; i < field.length; i++) {
			for(int g = 0; g < field[i].length; g++) {
				System.out.print(field[i][g] + " ");
			}
			System.out.println();
		}
	}
}
