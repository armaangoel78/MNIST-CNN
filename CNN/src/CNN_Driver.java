
public class CNN_Driver {		
	private int imageW = 28, imageH = 28;
	private int convOneLayers = 10, convTwoLayers = 10;
	private int poolSizeOne = 4, poolSizeTwo = 7;
	
	private double[][][][][] convInOne;
	private double[][][] convOutOne;
	private Filter[] filtersOne;
	private double[][][] pooledOne;
	private double[][][] pooledOneMaxes;

	
	private double[][][][][][] convInTwo;
	private double[][][][] convOutTwo;
	private Filter[] filtersTwo;
	private double[][][][] pooledTwo;
	private double[][][][] pooledTwoMaxes;
	
	private Neuron[][] neuronsOne;
	private Synapse[][][][] synapsesOne;
	
	private Neuron[] neuronsTwo;
	private Synapse[][][] synapsesTwo;
	
	private final double LEARNING_RATE  = .01; 
	private double[] errors;
	
	private double[] fc2_derivs;
	private double[][] fc1_derivs;
	private double[][][] cl2_derivs;

	public void run (double data[][], int[] labels, boolean firstSet) {
		if (firstSet) {
			init(data);
		}
		forewardProp(data);
		backwardProp(labels);
	}
	
	private void backwardProp(int[] labels) {
		errors = new double[neuronsTwo.length];
		for (int i = 0; i < errors.length; i++) {
			errors[i] = ((double) labels[i]) - neuronsTwo[i].getLastOut();
		}
		
		
		change_fc2();
		change_fc1();
		change_cl2();
		change_cl1();
	}
	
	private void change_cl1() {
		for (int a = 0; a < convOutTwo.length; a++) {
			for (int b = 0; b < convOutTwo[a].length; b++) {
				for (int c = 0; c < convOutOne[a].length; c++) {
					for (int d = 0; d < convOutOne[a][c].length; d++) {
						double adjustment = cl2_derivs[a][c/poolSizeOne][d/poolSizeOne] 
								* pooledOneMaxes[a][c][d] 
								* (convOutOne[a][c][d] == 0 ? 0 : 1)
								* 1/(Filter.getSize() * Filter.getSize());
						
						for (int e = 0; e < Filter.getSize(); e++) {
							for (int f = 0; f < Filter.getSize(); f++) {
								adjustment = adjustment * convInOne[a][c][d][e][f];
								filtersOne[a].adjust_filter(e, f, adjustment);
							}
						}
					}
				}
			}
		}
	}
	
	private void change_cl2() {
		cl2_derivs = new double [pooledOne.length][pooledOne[0].length][pooledOne[0][0].length];
				
		for (int i = 0; i < neuronsOne.length; i++) {
			for (int x = 0; x < neuronsOne[i].length; x++) {
				for (int a = 0; a < cl2_derivs.length; a++) {
					for (int b = 0; b < convOutTwo[a].length; b++) {
						for (int c = 0; c < convOutTwo[a][b].length; c++) {
							for (int d = 0; d < convOutTwo[a][b][c].length; d++) {
								double adjustment = 
										fc1_derivs[i][x] 
										* synapsesOne[i][x][a][b].getWeight()
										* pooledTwoMaxes[a][b][c][d] 
										* (convOutTwo[a][b][c][d] == 0 ? 0 : 1)
										* 1/(Filter.getSize() * Filter.getSize());
								
								for (int e = 0, e1 = -1; e < Filter.getSize(); e++, e1++) {
									for (int f = 0, f1 = -1; f < Filter.getSize(); f++, f1++) {
										if (c + e1 < pooledOne[0].length && 
											c + e1 >= 0 && 
											d + f1 < pooledOne[0][0].length && 
											d + f1 >= 0) 
												cl2_derivs[a][c + e1][d + f1] += adjustment * filtersTwo[b].getWeight(e, f);
										adjustment = adjustment * convInTwo[a][b][c][d][e][f];
										filtersTwo[b].adjust_filter(e, f, adjustment);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	
	private void change_fc1 () {
		fc1_derivs = new double[neuronsOne.length][neuronsOne[0].length];
		
		for (int i = 0; i < neuronsTwo.length; i++) {
			for (int a = 0; a < synapsesOne.length; a++) {
				for (int b = 0; b < synapsesOne[a].length; b++) {
					for (int c = 0; c < synapsesOne[a][b].length; c++) {
						for (int d = 0; d < synapsesOne[a][b][c].length; d++) {
							double adjustment = fc2_derivs[i] * synapsesTwo[i][a][b].getWeight() * getSigmoidDeriv(neuronsOne[a][b].getLastSum());
							fc1_derivs[a][b] += adjustment;
							
							adjustment = adjustment * pooledTwo[c][d][0][0];
							synapsesOne[a][b][c][d].adjustWeight(adjustment);
						}
					}
				}
			}
		}
	}
	
	
	private void change_fc2 () {
		fc2_derivs = new double[neuronsTwo.length];
		for (int a = 0; a < synapsesTwo.length; a++) {
			for (int b = 0; b < synapsesTwo[a].length; b++) {
				for (int c = 0; c < synapsesTwo[a][b].length; c++) {
					double adjustment = getSigmoidDeriv(neuronsTwo[a].getLastSum()) * errors[a] * LEARNING_RATE; 
					fc2_derivs[a] += adjustment;
					
					
					adjustment = adjustment * neuronsOne[b][c].getLastOut();
					synapsesTwo[a][b][c].adjustWeight(adjustment);
				}
			}
		}
	}
	
	private void init (double[][] data) {
		imageW = data.length;
		imageH = data[0].length;
		
		convInOne = new double[convOneLayers][imageW][imageH][Filter.getSize()][Filter.getSize()];
		convOutOne = new double[convOneLayers][imageW][imageH];
		filtersOne = new Filter[convOneLayers];
		pooledOne = new double[convOneLayers][imageW/poolSizeOne][imageH/poolSizeOne];
		pooledOneMaxes = new double[convOneLayers][imageW][imageH];
		
		convInTwo = new double[convOneLayers][convTwoLayers][imageW][imageH][Filter.getSize()][Filter.getSize()];
		convOutTwo = new double[convOneLayers][convTwoLayers][imageW/poolSizeOne][imageH/poolSizeOne];
		filtersTwo = new Filter[convTwoLayers];
		pooledTwo = new double[convOneLayers][convTwoLayers][(imageW/poolSizeOne)/poolSizeTwo][(imageH/poolSizeOne)/poolSizeTwo];
		pooledTwoMaxes = new double [convOneLayers][convTwoLayers][imageW/poolSizeOne][imageH/poolSizeOne];
		
		
		//Randomize filters for the first layer
		for (int i = 0; i < filtersOne.length; i++) {
			filtersOne[i] = new Filter();
			filtersOne[i].rand();
		}
		
		//Randomize filters for the second layer
		for (int i = 0; i < filtersTwo.length; i++) {
			filtersTwo[i] = new Filter();
			filtersTwo[i].rand();
		}
		
		neuronsOne = new Neuron[pooledTwo.length][pooledTwo[0].length];
		for (int i = 0; i < neuronsOne.length; i++) {
			for (int x = 0; x < neuronsOne[i].length; x++) {
				neuronsOne[i][x] = new Neuron();
			}
		}
		
		synapsesOne = new Synapse[neuronsOne.length][neuronsOne[0].length][pooledTwo.length][pooledTwo[0].length];
		for (int a = 0; a < synapsesOne.length; a++) {
			for (int b = 0; b < synapsesOne[a].length; b++) {
				for (int c = 0; c < synapsesOne[a][b].length; c++) {
					for (int d = 0; d < synapsesOne[a][b][c].length; d++) {
						synapsesOne[a][b][c][d] = new Synapse();
					}
				}
			}
		}
		
		neuronsTwo = new Neuron[neuronsOne.length];
		for (int i = 0; i < neuronsTwo.length; i++) {
			neuronsTwo[i] = new Neuron();
		}
		
		synapsesTwo = new Synapse[neuronsTwo.length][neuronsOne.length][neuronsOne[0].length];
		for (int a = 0; a < synapsesTwo.length; a++) {
			for (int b = 0; b < synapsesTwo[a].length; b++) {
				for (int c = 0; c < synapsesTwo[a][b].length; c++) {
					synapsesTwo[a][b][c] = new Synapse();
				}
			}
		}
		
	}
	
	
	private void forewardProp (double[][] data) {		
		for (int i = 0; i < convOneLayers; i++) {
			convOutOne[i] = getConvolutionOutput(data, filtersOne[i], convInOne[i]);
			pooledOne[i] = getPooledOutput(convOutOne[i], poolSizeOne, pooledOneMaxes[i]);
			for (int x = 0; x < convTwoLayers; x++) {
				convOutTwo[i][x] = getConvolutionOutput(pooledOne[i], filtersTwo[x], convInTwo[i][x]);
				pooledTwo[i][x] = getPooledOutput(convOutTwo[i][x], poolSizeTwo, pooledTwoMaxes[i][x]);
			}
		}
		
		for (int a = 0; a < synapsesOne.length; a++) {
			for (int b = 0; b < synapsesOne[a].length; b++) {
				for (int c = 0; c < synapsesOne[a][b].length; c++) {
					for (int d = 0; d < synapsesOne[a][b][c].length; d++) {
						synapsesOne[a][b][c][d].getOutput(pooledTwo[c][d][0][0]);
					}
				}
				neuronsOne[a][b].output(synapsesOne[a][b]);
			}
		}
		
		for (int a = 0; a < synapsesTwo.length; a++) {
			for (int b = 0; b < synapsesTwo[a].length; b++) {
				for (int c = 0; c < synapsesTwo[a][b].length; c++) {
					synapsesTwo[a][b][c].getOutput(neuronsOne[b][c].getLastOut());
				}
			}
			neuronsTwo[a].output(synapsesTwo[a]);
		}
		
	}
	
	private double[][] getConvolutionOutput (double input[][], Filter filter, double[][][][] input_store) {
		double[][] conv = new double[input.length][input[0].length];
		
		int stride = 1;
		for (int i = 0; i < conv.length; i+=stride){
			for (int x = 0; x < conv[i].length; x+=stride) {
				int size = Filter.getSize();
				double input_image[][] = new double[size][size];
				int padding = (Filter.getSize() - 1)/2;
				
				for (int a = i-padding, b = 0; a <= i+padding; a++, b++) {
					for (int c = x-padding, d = 0; c <= x+padding; c++, d++) {
						if (a < 0 || a > conv.length - 1 ||
							c < 0 || c > conv[i].length - 1) {
							input_image[b][d] = 0;
						}
						else input_image[b][d] = input[a][c];
					}
				}
				
				input_store[i][x] = input_image;
				double filtered = filter.applyFilter(input_image);
				conv[i][x] = filtered;
			}
		}
		return conv;
	}
	
	private double[][] getPooledOutput (double data[][], int poolSize, double[][] pooled_maxes) {
		double[][] pooled = new double[data.length/poolSize][data[0].length/poolSize];
		Pooler pooler = new Pooler();
		
		for (int i = 0; i < data.length; i+=poolSize){
			for (int x = 0; x < data[i].length; x+=poolSize) {
				double[][] input = new double[poolSize][poolSize];
				
				for (int a = 0; a < input.length; a++) {
					for (int b = 0; b < input[a].length; b++) {
						input[a][b] = data[i + a][x + b];
					}
				}
				
				pooled[i/poolSize][x/poolSize] = pooler.pool(input, pooled_maxes, i, x);
			}
		}
		return pooled;
	}
	
	private double getSigmoidDeriv(double input) { //derivative of sigmoid/logistic
		double numerator = Math.pow(2.7182818284590452353602874713527, input);
		double denomantor = Math.pow((numerator + 1), 2);
		
		return numerator/denomantor;
	}
	
	public double net_error () {
		double error = 0;
		
		for (int i = 0; i < errors.length; i++) {
			error += Math.abs(errors[i]);
		}
		
		return error;
	}
 	
	public int prediction() {
		int max_index = -1;
		double max_value = 0;
		for (int i = 0; i < neuronsTwo.length; i++) {
			if (neuronsTwo[i].getLastOut() > max_value) {
				max_index = i;
				max_value = neuronsTwo[i].getLastOut();
			} else if (neuronsTwo[i].getLastOut() == max_value) {
				max_index = -1;
			}
		}
		return max_index;
	}
	
}
