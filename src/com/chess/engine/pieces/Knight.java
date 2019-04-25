package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Board;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight extends Piece {

    //possible knight moves in 64 piece tile
    private static int[] CANDIATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};



    public Knight(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance, true);
    }

    public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.KNIGHT,piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {


        final List<Move> legalMoves = new ArrayList<>();

        //try every possible moves. We do not know that if every possible moves are valid yet.
        for( final int currentCandiateOffset : CANDIATE_MOVE_COORDINATES){

            //try each possible moves according to current coordinate
           final int candidateDestinationCoordinate = this.piecePosition + currentCandiateOffset;

            // check if the possible move is out of board
            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){


                //check out of border position
                if(isFirstColumnExclusion(this.piecePosition, currentCandiateOffset) ||
                    isSecondColumnExclusion(this.piecePosition, currentCandiateOffset) ||
                    isSeventhColumnExclusion(this.piecePosition, currentCandiateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandiateOffset)){
                    continue;
                }
                //get the possible move's tile coordinate from board
                final Tile candiateDestinationTile = board.getTile(candidateDestinationCoordinate);

                //check if coordinate is occupied
                if(!candiateDestinationTile.isTileOccupied()){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }
                //if coordinates occupied
                else {

                    final Piece pieceAtDestination = candiateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    //check that occupant is enemy. If enemy you might attack
                    if(this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(final Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString(){
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){

        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset ==15);
    }

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6 );

    }

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10 );

    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -15 || candidateOffset == -6 ||
                candidateOffset == 10 || candidateOffset == 17);

    }

}
