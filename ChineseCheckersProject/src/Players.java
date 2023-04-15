/**
 *                   Players Enum
 * The Players Enum represents the players in the game
 *
 */
public enum Players {
        RED_PLAYER,
        GREEN_PLAYER;

        @Override
        public String toString() {
                switch (this) {
                        case RED_PLAYER:
                                return "Red Player";
                        case GREEN_PLAYER:
                                return "Green Player";
                        default:
                                throw new IllegalArgumentException();
                }
        }

}
