package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	//Inner class to represent Feet measurement
    public static class Feet {
    	//final varible
        private final double value;

        //constructor
        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
        	//checking object
            if (this == obj) return true;
            //checking if objects of same class 
            if (obj == null || getClass() != obj.getClass()) return false;
            //converting now
            Feet feet = (Feet) obj;
            return Double.compare(feet.value, this.value) == 0;
        }
    }

    public static void main(String[] args) {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);
        System.out.println("Equal: " + feet1.equals(feet2));
    }
}