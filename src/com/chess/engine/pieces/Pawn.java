package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    public final static int[] CANDIATE_MOVE_VECTOR_COORDINATES = {8, 16, 7, 9};


    Pawn(final int piecePosition, final Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();


        for(int currentCandidateOffset :  CANDIATE_MOVE_VECTOR_COORDINATES){

            //get alliance color of piece so we could decide which way pawns should go(if white go up, otherwise down)
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);

            if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                //TODO more work here!!(deal with promotions)
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                //if pawn is about to make first moves with two sqaures
            }else if(currentCandidateOffset == 16 && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())){
                //get the two more squares destination to check
                    final int behindCandidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * 8);

                    //if the next and next next pile is avaible, make move
                    if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                            !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
              //edge cases
            } else if( currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition]) && this.pieceAlliance.isWhite() ||
                    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
                final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                    //TODO more work here - add attack move
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }

            } else if( currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition]) && this.pieceAlliance.isWhite() ||
                     (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))){
                final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()){
                    //TODO more work here - add attack move
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }

            }
        }

        return ImmutableList.copyOf(legalMoves);
    }
}
