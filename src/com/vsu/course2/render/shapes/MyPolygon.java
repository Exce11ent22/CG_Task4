package com.vsu.course2.render.shapes;

import com.vsu.course2.render.point.MyPoint;
import com.vsu.course2.render.point.MyVector;
import com.vsu.course2.render.point.PointConverter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyPolygon {

    private static final double AmbientLight = 0.05;

    private Color baseColor, lightingColor;

    private MyPoint[] points;

    public MyPolygon(Color color, MyPoint... points) {
        this.baseColor = this.lightingColor = color;
        this.points = new MyPoint[points.length];
        for(int i = 0; i < points.length; i++){
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }

    public MyPolygon(MyPoint... points) {
        this.baseColor = this.lightingColor = Color.WHITE;
        this.points = new MyPoint[points.length];
        for(int i = 0; i < points.length; i++){
            MyPoint p = points[i];
            this.points[i] = new MyPoint(p.x, p.y, p.z);
        }
    }

    public void render(Graphics g) {
        Polygon poly = new Polygon();
        for(int i = 0; i < this.points.length; i++) {
            Point p = PointConverter.convertPoint(this.points[i]);
            poly.addPoint(p.x, p.y);
        }

        g.setColor(this.lightingColor);
        g.fillPolygon(poly);
    }

    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector){
        for(MyPoint p : points){
            PointConverter.rotateAxisX(p, CW, xDegrees);
            PointConverter.rotateAxisY(p, CW, yDegrees);
            PointConverter.rotateAxisZ(p, CW, zDegrees);
        }

        this.updateLigtingRatio(lightVector);
    }

    public double getAverageX(){
        double sum = 0;
        for(MyPoint p : this.points){
            sum += p.x;
        }

        return sum/ this.points.length;
    }

    public void setColor(Color color){
        this.baseColor = color;
    }

    public static MyPolygon[] sortPolygons(MyPolygon[] polygons){
        List<MyPolygon> polygonList = new ArrayList<>();

        for(MyPolygon poly : polygons){
            polygonList.add(poly);
        }

        Collections.sort(polygonList, new Comparator<MyPolygon>() {
            @Override
            public int compare(MyPolygon p1, MyPolygon p2) {
                double p1AvarageX = p1.getAverageX();
                double p2AvarageX = p2.getAverageX();
                double diff = p2AvarageX - p1AvarageX;
                if(diff == 0){
                    return 0;
                }
                return diff < 0 ? 1 : -1;
            }
        });

        for(int i = 0; i < polygons.length; i++){
            polygons[i] = polygonList.get(i);
        }

        return polygons;
    }

    private void updateLigtingRatio(MyVector lightVector){
        if(this.points.length < 3){
            return;
        }

        MyVector v1 = new MyVector(this.points[0], this.points[1]);
        MyVector v2 = new MyVector(this.points[1], this.points[2]);
        MyVector normal = MyVector.normalize(MyVector.cross(v1, v2));
        double dot = MyVector.dot(normal, lightVector);
        double sign = dot < 0 ? -1 : 1;
        dot = sign * dot * dot;
        dot = (dot + 1) / 2 * 0.8;

        double lightRatio = Math.min(1, Math.max(0, AmbientLight + dot));
        this.updateLightingColor(lightRatio);
    }

    public void updateLightingColor(double lightRatio){
        int red = (int) (this.baseColor.getRed() * lightRatio);
        int green = (int) (this.baseColor.getGreen() * lightRatio);
        int blue = (int) (this.baseColor.getBlue() * lightRatio);
        //this.lightingColor = new Color(red/255f, green/255f, blue/255f, 0.5f);
        this.lightingColor = new Color(red, green, blue);
    }

}
