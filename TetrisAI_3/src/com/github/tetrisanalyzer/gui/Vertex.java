package com.github.tetrisanalyzer.gui;

public class Vertex {
    public double x;
    public double y;

    public Vertex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vertex normalizeX(double adjustX, double scale) {
        return new Vertex((x + adjustX) * scale, y);
    }

    public Vertex resize(double wx1, double wy1, double wx2, double wy2, int width, int height) {
        return new Vertex(
                (x - wx1) / (wx2 - wx1) * width,
                (y - wy1) / (wy2 - wy1) * height);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (Double.compare(vertex.x, x) != 0) return false;
        if (Double.compare(vertex.y, y) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
