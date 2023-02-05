package eliascregard.game;

import eliascregard.physics.Vector2D;

import java.awt.image.BufferedImage;

public record GameObject(double depth, BufferedImage wallColumn, Vector2D position) {}
