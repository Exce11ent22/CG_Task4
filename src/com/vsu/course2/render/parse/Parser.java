package com.vsu.course2.render.parse;

import com.vsu.course2.render.point.MyPoint;
import com.vsu.course2.render.shapes.MyPolygon;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Parser {

    public static MyPolygon[] getModel(String path, double size, double centerX, double centerY, double centerZ) throws IOException {
        List<MyPolygon> polygonList = new ArrayList<>();
        List<MyPoint> pointList = new ArrayList<>();


        List<String> file = Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.toList());

        for (String line : file) {
            if (line.length() == 0) continue;

            if(line.charAt(0) == 'v' && line.charAt(1) != 'n'){
                String[] symbols = line.split(" ");
                int sizeS = symbols.length;
                pointList.add(new MyPoint(
                        Double.parseDouble(symbols[sizeS - 3]) * size + centerX,
                        Double.parseDouble(symbols[sizeS - 2]) * size + centerY,
                        Double.parseDouble(symbols[sizeS - 1]) * size + centerZ));
            }

        }

        MyPoint[] points = new MyPoint[pointList.size()];
        points = pointList.toArray(points);

        for (String line : file){
            if (line.length() == 0) continue;

            if(line.charAt(0) == 'f'){
                String[] symbols = line.strip().split(" ");
                MyPoint[] pointsForPolygon = new MyPoint[symbols.length-2];
                for (int i = 0; i < symbols.length - 2; i++){
                    pointsForPolygon[i] = points[Integer.parseInt(symbols[i+2].split("//")[0]) - 1];
                }
                polygonList.add(new MyPolygon(pointsForPolygon));
            }

        }

        MyPolygon[] polygons = new MyPolygon[polygonList.size()];
        polygons = polygonList.toArray(polygons);
        return polygons;
    }

}
