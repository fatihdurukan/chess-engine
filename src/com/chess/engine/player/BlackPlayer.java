package com.chess.engine.player;

import com.chess.engine.Alliance;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.chess.engine.board.Board;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BlackPlayer extends Player{
    public BlackPlayer(final Board board,
                       final Collection<Move> whiteStandardLegalMoves,
                       final Collection<Move> blackStandardLegalMoves) {
        super(board,blackStandardLegalMoves,whiteStandardLegalMoves);

    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getBlackPieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.BLACK;
    }

    @Override
    public Player getOpponent() {
        return this.board.whitePlayer();
    }

    @Override
    protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
                                                    final Collection<Move> opponentLegals) {
        /*
        -The king and the chosen rook are on the player's first rank.
        -Neither the king nor the chosen rook has previously moved.
        -There are no pieces between the king and the chosen rook.
        -The king is not currently in check.
        -The king does not pass through a square that is attacked by an enemy piece.
        -The king does not end up in check. (True of any legal move.)
         */

        final List<Move> kingCastles = new ArrayList<>();

        if(this.playerKing.isFirstMove() && !this.isInCheck()){
            //black king side castle
            if(!this.board.getTile(5).isTileOccupied() && this.board.getTile(6).isTileOccupied()){
                final Tile rookTile = this.board.getTile(7);

                if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentLegals).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook()) {

                        kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6,
                                                                    (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(),
                                                                    5));
                    }
                }
            }
        }

        if(!this.board.getTile(1).isTileOccupied() &&
                !this.board.getTile(2).isTileOccupied()&&
                !this.board.getTile(3).isTileOccupied()){

            final Tile rookTile = this.board.getTile(0);

            if(rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()&&
                    Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() &&
                    Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()){

                kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2,
                                                             (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(),
                                                             3));
            }
        }
        return ImmutableList.copyOf(kingCastles);
    }
}
