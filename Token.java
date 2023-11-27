public class Token {

    private final TipoToken tipo;
    private final String lexema;
    private final int posicion;

    public Token(TipoToken tipo, String lexema, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.posicion = posicion;
    }

    public Token(TipoToken tipo, String lexema) {
        this(tipo, lexema, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token = (Token) o;

        if (posicion != token.posicion) return false;
        if (tipo != token.tipo) return false;
        return lexema != null ? lexema.equals(token.lexema) : token.lexema == null;
    }

    @Override
    public int hashCode() {
        int result = tipo != null ? tipo.hashCode() : 0;
        result = 31 * result + (lexema != null ? lexema.hashCode() : 0);
        result = 31 * result + posicion;
        return result;
    }

    @Override
    public String toString() {
        return String.format("%s %s (pos %d)", tipo, lexema, posicion);
    }
}
