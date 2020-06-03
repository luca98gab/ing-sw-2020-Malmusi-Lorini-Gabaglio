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

    public static Boolean checkCanBuildW(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getX()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX()-1 == 0 || pawn.getX()-1 == 4 || pawn.getY() == 0 || pawn.getY() == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
                return true;
            }

        }
        return false;
    }
    public static Boolean checkCanBuildE(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getX()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX()+1 == 0 || pawn.getX()+1 == 4 || pawn.getY() == 0 || pawn.getY() == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildN(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()-1];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX() == 0 || pawn.getX() == 4 || pawn.getY()-1 == 0 || pawn.getY()-1 == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildS(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()][pawn.getY()+1];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX() == 0 || pawn.getX() == 4 || pawn.getY()+1 == 0 || pawn.getY()+1 == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildNW(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getX()>0 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()-1];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX()-1 == 0 || pawn.getX()-1 == 4 || pawn.getY()-1 == 0 || pawn.getY()-1 == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildNE(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getX()<4 && pawn.getY()>0){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()-1];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX()+1 == 0 || pawn.getX()+1 == 4 || pawn.getY()-1 == 0 || pawn.getY()-1 == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildSE(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getX()<4 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()+1][pawn.getY()+1];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX()+1 == 0 || pawn.getX()+1 == 4 || pawn.getY()+1 == 0 || pawn.getY()+1 == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
                return true;
            }
        }
        return false;
    }
    public static Boolean checkCanBuildSW(Game game, Pawn pawn, Cell restriction, Boolean edgeCellsAllowed){
        if (pawn.getX()>0 && pawn.getY()<4){
            Cell currentCell = game.getMap()[pawn.getX()][pawn.getY()];
            Cell newCell = game.getMap()[pawn.getX()-1][pawn.getY()+1];
            Boolean newCellIsOnBorder = false;
            if (pawn.getX()-1 == 0 || pawn.getX()-1 == 4 || pawn.getY()+1 == 0 || pawn.getY()+1 == 4) newCellIsOnBorder = true;
            if (edgeCellsAllowed){
                if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false)){
                    return true;
                }
            } else if (!newCell.equals(restriction) && newCell.getIsFull() == null && newCell.getHasDome().equals(false) && !newCellIsOnBorder){
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

    public static Boolean checkAresPower(Game game, Pawn pawn){
        if (pawn.getId()==1) pawn= pawn.getPlayer().getPawns()[1];
        else pawn= pawn.getPlayer().getPawns()[0];
        for (int i=pawn.getY()-1; i<pawn.getY()+2; i++){
            if (i>=0) {
                for (int j=pawn.getX()-1; j<pawn.getX()+2; j++){
                    if( j>=0 && game.getMap()[j][i].getFloor()>0 && !game.getMap()[j][i].getHasDome() && game.getMap()[j][i].getIsFull()==null) return true;
                }
            }
        }
        return false;
    }

}
