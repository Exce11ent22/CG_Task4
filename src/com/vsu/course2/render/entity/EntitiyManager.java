package com.vsu.course2.render.entity;

import com.vsu.course2.render.entity.builder.BasicEntityBuilder;
import com.vsu.course2.render.entity.builder.ComplexEntityBuilder;
import com.vsu.course2.render.input.ClickType;
import com.vsu.course2.render.input.Mouse;
import com.vsu.course2.render.point.MyPoint;
import com.vsu.course2.render.point.MyVector;
import com.vsu.course2.render.point.PointConverter;
import com.vsu.course2.render.shapes.MyPolygon;
import com.vsu.course2.render.shapes.Tetrahedron;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EntitiyManager {

    private List<IEntity> entities;
    private int initialX, initialY;
    private double mouseSensitivity = 2.5;
    private MyVector lightVector = MyVector.normalize(new MyVector(1,0,0));

    public EntitiyManager(){
        this.entities = new ArrayList<>();
    }

    public void init() throws IOException {
        //this.entities.add(BasicEntityBuilder.createCube(100,0,0,0));
        //this.entities.add(ComplexEntityBuilder.createRubiksCube(100, 0, 0, 0));
        //this.entities.add(BasicEntityBuilder.createDiamond(new Color(115, 236, 255),100,0,0,0));
        this.entities.add(BasicEntityBuilder.createKleinBottle(new Color(115, 236, 255),100,0,0,-250));
        //this.entities.add(BasicEntityBuilder.createDodecahedron(Color.RED, 100,0,0,0));
    }

    public void update(Mouse mouse){
        int x = mouse.getX();
        int y = mouse.getY();
        if(mouse.getButton() == ClickType.LeftClick){
            int xDif = x - initialX;
            int yDif = y - initialY;

            this.rotate(true, 0,-yDif/mouseSensitivity,-xDif/mouseSensitivity);
        } else if(mouse.getButton() == ClickType.RightClick){
            int xDif = x - initialX;

            this.rotate(true, xDif/mouseSensitivity, 0, 0);
        }

        if(mouse.isScrollingUp()){
            PointConverter.zoomIn();
        } else if (mouse.isScrollingDown()){
            PointConverter.zoomOut();
        }

        mouse.resetScroll();
        initialX = x;
        initialY = y;
    }

    public void render(Graphics g){
        for(IEntity entity : this.entities){
            entity.render(g);
        }
    }

    private void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees){
        for(IEntity entity : this.entities){
            entity.rotate(CW, xDegrees, yDegrees, zDegrees, this.lightVector);
        }
    }

}
