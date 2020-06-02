package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;
import it.polimi.ingsw.PSP32.server.ClientHandler;

import java.io.IOException;

public class CheckCanBuild {





    /** A series of methods to check for valid build locations
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @param restriction : Cell possible restricted cells
     * @return Boolean (True= you can build there, False= you can't build there)
     */

    public static Boolean checkCanBuildW(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildE(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildN(Game game, Pawn pawn, Cell restriction){
        if (pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()-1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildS(Game game, Pawn pawn, Cell restriction){
        if (pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()+1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildNW(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()>0 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()-1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildNE(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()<4 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()-1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildSE(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()<4 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()+1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildSW(Game game, Pawn pawn, Cell restriction){
        if (pawn.getX()>0 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()+1];
            if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildBelow(Game game, Pawn pawn, Cell restriction){
        Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
        if (currentCell.getFloor()==3) return false;
        else if (pawn.getPlayer().getGod().getName().equals("Zeus")) return true;
        return false;

    }

}
