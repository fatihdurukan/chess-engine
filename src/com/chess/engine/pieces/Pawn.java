package com.chess.engine.pieces;

import com.chess.engine.board.Move;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move.*;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    public final static int[] CANDIATE_MOVE_VECTOR_COORDINATES = {8, 16, 7, 9};


    public Pawn(final Alliance pieceAlliance, final int piecePosition) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, true);
    }

    public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();


        for (int currentCandidateOffset : CANDIATE_MOVE_VECTOR_COORDINATES) {

            //get alliance color of piece so we could decide which way pawns should go(if white go up, otherwise down)
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAllegiance().getDirection() * currentCandidateOffset);

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {

                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this,candidateDestinationCoordinate)));
                }else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }

                //if pawn is about to make first moves with two squares
            } else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.getPieceAllegiance().isBlack()) ||
                            (BoardUtils.SECOND_RANK[this.piecePosition] && this.getPieceAllegiance().isWhite()))) {
                //get the two more squares destination to check
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.getPieceAllegiance().getDirection() * 8);

                //if the next and next next pile is avaible, make move
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
                //edge cases
            } else if (currentCandidateOffset == 7 &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition]) && this.pieceAlliance.isWhite() ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                         if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAllegiance()) {
                            if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                                legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                            }else {
                                legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                            }
                    }
                    } else if (board.getEnPassantPawn() != null) {
                        if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                            final Piece pieceOnCandidateEnPassant = board.getEnPassantPawn();
                            if (this.pieceAlliance != pieceOnCandidateEnPassant.getPieceAllegiance()) {
                                legalMoves.add(new PawnEnPassanAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidateEnPassant));
                            }
                        }
                }

                } else if (currentCandidateOffset == 9 &&
                        !((BoardUtils.FIRST_COLUMN[this.piecePosition]) && this.pieceAlliance.isWhite() ||
                                (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                    if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                        final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                        if (this.pieceAlliance != pieceOnCandidate.getPieceAllegiance()) {

                            if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)){
                                legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate)));
                            }else {
                                legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
                            }

                        }
                    }
                    else if (board.getEnPassantPawn() != null) {
                        if (board.getEnPassantPawn().getPiecePosition() == (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                            final Piece pieceOnCandidateEnPassant = board.getEnPassantPawn();
                            if (this.pieceAlliance != pieceOnCandidateEnPassant.getPieceAllegiance()) {
                                legalMoves.add(new PawnEnPassanAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidateEnPassant));
                            }
                        }
                    }
                }
            }

        return ImmutableList.copyOf(legalMoves);
    }

        @Override
        public Pawn movePiece ( final Move move){
            return new Pawn(move.getMovedPiece().getPieceAllegiance(), move.getDestinationCoordinate());
        }


        @Override
        public String toString () {
            return PieceType.PAWN.toString();
        }

        public Piece getPromotionPiece() {
        return new Queen(this.pieceAlliance, this.piecePosition,false);
        }
    }
