package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

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

   
    
    
    //Inches Inner classs for inch operations
    public static class Inches {
    	//final varible
        private final double value;

        //constructor
        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
        	//checking object
            if (this == obj) return true;
            //checking if objects of same class 
            if (obj == null || getClass() != obj.getClass()) return false;
            //converting now
            Inches feet = (Inches) obj;
            return Double.compare(feet.value, this.value) == 0;
        }
    }
    
    //method to represent feet equality
    
    public static void demonstrateFeetEquality() {
    	Feet feet1=new Feet(1.0);
    	Feet feet2=new Feet(1.0);
    	System.out.println("Input: 1.0 ft and 1.0 ft");
    	System.out.println("Output: Equal ("+feet1.equals(feet2)+")");
    }
    
    //method to represent inch equality
    
    public static void demonstrateInchesEquality() {
    	Inches inch1=new Inches(1.0);
    	Inches inch2=new Inches(1.0);
    	System.out.println("Input: 1.0 ft and 1.0 ft");
    	System.out.println("Output: Equal ("+inch1.equals(inch2)+")");
    }
    
    
    //The main method
    public static void main(String[] args) {
        demonstrateFeetEquality();
        demonstrateFeetEquality();
    }
}