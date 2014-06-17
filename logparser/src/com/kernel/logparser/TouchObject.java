package com.kernel.logparser;

public class TouchObject{
	
	public static class TimeStampObject {
		public int mmdd;
		public double time;
		public TimeStampObject(int mmdd, double f) {
			//super();
			this.mmdd = mmdd;
			this.time = f;
		}
	}

	public TimeStampObject t;
	private float X;
	private float Y;
	private int finger;
	private boolean dir;
	private int pressure;
	
	public float getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public float getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public int getFinger() {
		return finger;
	}
	public void setFinger(int finger) {
		this.finger = finger;
	}
	public boolean isDir() {
		return dir;
	}
	public void setDir(boolean dir) {
		this.dir = dir;
	}
	public TimeStampObject getT() {
		return t;
	}
	public void setT(TimeStampObject T) {
		this.t= T;
	}
	public int getPressure() {
		return pressure;
	}
	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
	
	public TouchObject(TouchObject t){
		super();
	}
	public TouchObject(TimeStampObject T, int x, int y, int finger, boolean dir, int pressure) {
		super();
		this.t = T;
		X = x;
		Y = y;
		this.finger = finger;
		this.dir = dir;
		this.pressure = pressure;
	}
	public TouchObject(TimeStampObject T,int x, int y, int finger, boolean dir) {
		super();
		this.t=T;
		X = x;
		Y = y;
		this.finger = finger;
		this.dir = dir;
	}
	public static String display(TouchObject T){
		if(T.dir){
			if(10 ==T.finger)
				return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PALM DETECT "+T.pressure).toString();
				//System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PRESS EVENT "+T.pressure);
			else if(T.finger>10)
				return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" KEYPRESS EVENT "+T.pressure).toString();
			else
				return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PRESS EVENT "+T.pressure).toString();
		}
		else{
			if(10==T.finger)
				return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" PALM RELEASE ").toString();
			else if(T.finger>10)
				return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" KEYRELEASE EVENT ").toString();
			else
				//System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" RELEASE EVENT");
				return (T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.finger+" RELEASE EVENT ").toString();
		}
	}
	
	public static void displayObject(TouchObject T){
		if(T.dir){
			System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+T.pressure+" PRESS EVENT "+T.finger);
			}
	
		else{
			System.out.println(T.t.mmdd+" "+T.t.time+" "+T.X+" "+T.Y+" "+" RELEASE EVENT "+T.finger);
		}
	}	
		public static void clearObject(TouchObject T){
			T.dir=true;
			T.X=T.Y=T.finger=T.pressure=T.t.mmdd=0;
			T.t.time=0;
		}
	}

