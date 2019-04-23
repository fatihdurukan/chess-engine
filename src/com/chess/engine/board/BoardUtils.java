package com.chess.engine.board;

public class BoardUtils {

    //all the piles are false except the first column
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN =  initColumn(7);

    public static final boolean[] SECOND_ROW = initRow(8);
    public static final boolean[] SEVENTH_ROW = initRow(48);


    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    //BoardUtils is not meant to instantiate so throw exception
    private BoardUtils(){
        throw new RuntimeException("You cannot instantiate me!");
    }


    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do{
            row[rowNumber] = true;
            rowNumber++;
        }while(rowNumber % NUM_TILES_PER_ROW != 0);

        return row;
    }

    private static boolean[] initColumn(int columnNumber) {

        final boolean[] column = new boolean[NUM_TILES];

        do{
            column[columnNumber] = true;
            columnNumber += NUM_TILES_PER_ROW;
        } while (columnNumber < NUM_TILES);

        return column;
    }

    public static boolean isValidTileCoordinate(final int coordinate) {

        return coordinate >= 0 && coordinate < NUM_TILES;
    }
}
