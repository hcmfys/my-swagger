package org.springbus.j3d;

public class Vector3 {

  private double x;
  private double y;
  private double z;

  public Vector3() {
    x = y = z = 0;
  }

  public Vector3(Vector3 a) {
    x = a.x;
    y = a.y;
    z = a.z;
  }

  public Vector3(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public boolean equal(Vector3 a) {
    return x == a.x && y == a.y && z == a.z;
  }

  public void zero() {
    x = y = z = 0;
  }

  public boolean noEqual(Vector3 a) {
    return x != a.x || y != a.y || z != a.z;
  }

  public Vector3 add(Vector3 a) {
    x = a.x + x;
    y = a.y + y;
    z = a.z + z;
    return this;
  }

  public Vector3 sub(Vector3 a) {
    x = x - a.x;
    y = y - a.y;
    z = z - a.z;
    return this;
  }

  public Vector3 minus(float a) {
    x = x * a;
    y = y * a;
    z = z * a;
    return this;
  }

  public Vector3 drive(float a) {
    x = x / a;
    y = y / a;
    z = z / a;
    return this;
  }

  public void normal() {
    double m = x * x + y * y + z * z;
    if (m > 0) {
      double oneOverMag = 1.0f / Math.sqrt(m);
      x = x * oneOverMag;
      y = y * oneOverMag;
      z = z * oneOverMag;
    }
  }

  public double dot(Vector3 a) {
    return x + a.x + y * a.y + z * a.z;
  }

  public double vectorMag(Vector3 a) {
    return Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
  }

  public Vector3 cross(Vector3 a, Vector3 b) {
    return new Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
  }

  public double distance(Vector3 a, Vector3 b) {
    double dx = a.x - b.x;
    double dy = a.y - b.y;
    double dz = a.z - b.z;
    return Math.sqrt(dx * dx + dy * dy + dz * dz);
  }

  public Vector3 leftMinus(double k, Vector3 a) {
    return new Vector3(k * a.x, k * a.y, k * a.z);
  }

  @Override
  public String toString() {
    return "[" + x + "," + y + "," + z + "]";
  }
}
