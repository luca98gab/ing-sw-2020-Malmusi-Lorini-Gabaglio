package it.polimi.ingsw.PSP32.controller;

import it.polimi.ingsw.PSP32.model.Cell;
import it.polimi.ingsw.PSP32.model.Game;
import it.polimi.ingsw.PSP32.model.Pawn;
import it.polimi.ingsw.PSP32.model.Player;

import java.io.IOException;

public class CheckCanMove {



    /** A series of methods to check for valid move locations
     *
     * @param game : Game
     * @param pawn : Pawn active pawn
     * @param restriction : Cell possible restricted cells
     * @return Boolean (True= you can move there, False= you can't move there)
     */

    public static Boolean checkCanMoveW(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()-2][pawn.getY()];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveE(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()+2][pawn.getY()];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveN(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()-1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getY()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()][pawn.getY()-2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveS(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()+1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getY()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()][pawn.getY()+2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveNW(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()){
            climbableFloor = 0;
        }
        if (pawn.getX()>0 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()-1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()-1>0 && pawn.getY()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()-2][pawn.getY()-2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveNE(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()<4 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()-1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()+1<4 && pawn.getY()-1>0){
                Cell opponentNewCell = game.getMap()[pawn.getX()+2][pawn.getY()-2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveSE(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()<4 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()+1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()+1<4 && pawn.getY()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()+2][pawn.getY()+2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
    public static Boolean checkCanMoveSW(Game game, Pawn pawn, Cell restriction){
        String god = pawn.getPlayer().getGod().getName();
        int climbableFloor = 1;
        if (game.getAthenaFlag()==true){
            climbableFloor = 0;
        }
        if (pawn.getX()>0 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()+1];
            if (!newCell.equals(restriction) && (newCell.getIsFull() == null || (god.equals("Apollo") && !newCell.getIsFull().getPlayer().getGod().getName().equals("Apollo"))) && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && newCell.getHasDome().equals(false)){
                return true;
            } else if (god.equals("Minotaur") && newCell.getFloor() < currentCell.getFloor()+1+climbableFloor && !newCell.getHasDome() && pawn.getX()-1>0 && pawn.getY()+1<4){
                Cell opponentNewCell = game.getMap()[pawn.getX()-2][pawn.getY()+2];
                if (!opponentNewCell.getHasDome() && opponentNewCell.getIsFull()==null){
                    return true;
                }
            }
        }
        return false;
    }
}
