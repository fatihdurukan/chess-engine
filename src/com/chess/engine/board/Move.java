package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

import static com.chess.engine.board.Board.*;

public abstract class Move {

    final Board board;
    final Piece movedPiece;
    final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    public Move(final Board board,
                final Piece movedPiece,
                final int destinationCoordinate){
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }

    public int getDestinationCoordinate(){
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }

    public Board execute(){
        final Builder builder = new Builder();

        //place all pieces to new board except the one moves
        for( Piece piece : this.board.getCurrentPlayer().getActivePieces()){
           //TODO hashcode and equals for pieces
            if(!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }

        //place all enemy pieces to new board
        for( Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
        }

        //move the moved piece!
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

        return builder.build();
    }



    //SUB-CLASSES
    public static final class MajorMove extends Move{

        public MajorMove(final Board board,
                         final Piece piece,
                         final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

    }



    //SUB-CLASSES
    public static class AttackMove extends Move{

        final Piece attackedPiece;


        public AttackMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board, piece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute(){
            return null;
        }
    }

    //SUB-CLASSES
    public static final class PawnMove extends Move{

        public PawnMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

    }

    //SUB-CLASSES
    public static class PawnAttackMove extends AttackMove{

        public PawnAttackMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinate,
                        final Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }

    }

    //SUB-CLASSES
    public static final class PawnEnPassanAttackMove extends PawnAttackMove{

        public PawnEnPassanAttackMove(final Board board,
                              final Piece piece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board, piece, destinationCoordinate, attackedPiece);
        }

    }

    //SUB-CLASSES
    public static final class PawnJump extends Move{

        public PawnJump(final Board board,
                        final Piece piece,
                        final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

    }

    //SUB-CLASSES
    static abstract class CastleMove extends Move{

        public CastleMove(final Board board,
                          final Piece piece,
                          final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

    }

    //SUB-CLASSES
    public static final class KingSideCastleMove extends CastleMove{

        public KingSideCastleMove(final Board board,
                        final Piece piece,
                        final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

    }

    //SUB-CLASSES
    public static final class QueenSideCastleMove extends CastleMove{

        public QueenSideCastleMove(final Board board,
                                   final Piece piece,
                                   final int destinationCoordinate) {
            super(board, piece, destinationCoordinate);
        }

    }

    //SUB-CLASSES
    public static final class NullMove extends Move{

        public NullMove() {
            super(null, null,-1);
        }

        @Override
        public Board execute(){
            throw new RuntimeException("Can not execute to null move!");
        }

    }

    public static class MoveFactory{

        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }


        public static Move createMove(final Board board,
                                      final int currentCoordinate,
                                      final int destinationCoordinate){

            for ( final Move move: board.getAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate &&
                    move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }

    }
}
