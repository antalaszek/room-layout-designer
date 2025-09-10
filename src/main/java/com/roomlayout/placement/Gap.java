package com.roomlayout.placement;

public final class Gap {
    public static final Gap NO_GAP = new Gap(0.0);
    
    private final double value;
    
    private Gap(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Gap cannot be negative");
        }
        this.value = value;
    }
    
    public static Gap of(double value) {
        return value == 0.0 ? NO_GAP : new Gap(value);
    }
    
    public double getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gap gap = (Gap) obj;
        return Double.compare(gap.value, value) == 0;
    }
    
    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
    
    @Override
    public String toString() {
        return String.format("Gap(%.2fm)", value);
    }
}