package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> opponentMoves){

        this.board = board;
        this.playerKing = establishKing();
        this.legalMoves =  legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    //check if the piece position is in the list of opponents moves list
    private static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        final List<Move> attackMove = new ArrayList<>();
        for( final Move move : moves){
            if(piecePosition == move.getDestinationCoordinate()){
                attackMove.add(move);
            }
        }
        return ImmutableList.copyOf(attackMove);
    }

    private King establishKing(){

        for(final Piece piece : getActivePieces() ){
            if(piece.getPieceType().isKing()){
                return (King) piece;
            }
        }

        throw new RuntimeException("Should not reach here! Not a valid board");
    }

    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }

    public boolean isMoveLegal(final Move move){
       return this.legalMoves.contains(move);
    }


    public boolean isInCheck(){
        return this.isInCheck;
    }


    public boolean isInCheckMate(){
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate(){
        return !this.isInCheck && !hasEscapeMoves();
    }


    public boolean isCastled(){
        return false;
    }

    public King getPlayerKing(){
        return this.playerKing;
    }



    protected boolean hasEscapeMoves() {
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }


    public MoveTransition makeMove(final Move move){
        
        if(!isMoveLegal(move)){
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.getCurrentPlayer().getLegalMoves());

        if(!kingAttacks.isEmpty()){
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(this.board, move, MoveStatus.DONE);


    }



    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
}
