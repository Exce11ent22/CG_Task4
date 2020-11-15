package com.vsu.course2.render.entity;

import com.vsu.course2.render.point.MyVector;
import com.vsu.course2.render.shapes.MyPolygon;
import com.vsu.course2.render.shapes.Tetrahedron;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Entity implements IEntity{

    private List<Tetrahedron> tetrahedrons;
    private MyPolygon[] polygons;

    public Entity(List<Tetrahedron> tetrahedrons){
        this.tetrahedrons = tetrahedrons;
        List<MyPolygon> tempList = new ArrayList<>();
        for(Tetrahedron tetra : this.tetrahedrons){
            tempList.addAll(Arrays.asList(tetra.getPolygons()));
        }
        this.polygons = new MyPolygon[tempList.size()];
        this.polygons = tempList.toArray(this.polygons);
        this.sortPolygons();
    }

    @Override
    public void render(Graphics g) {
        for(MyPolygon poly : this.polygons){
            poly.render(g);
        }
    }

    @Override
    public void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector) {
        for(Tetrahedron tetra : this.tetrahedrons){
            tetra.rotate(CW, xDegrees, yDegrees, zDegrees, lightVector);
        }
        this.sortPolygons();
    }

    private void sortPolygons(){
        MyPolygon.sortPolygons(this.polygons);
    }
}
