package com.vsu.course2.render.entity;

import com.vsu.course2.render.point.MyVector;

import java.awt.*;

public interface IEntity {

    void render(Graphics g);

    void rotate(boolean CW, double xDegrees, double yDegrees, double zDegrees, MyVector lightVector);

}
