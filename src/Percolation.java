/**
 * Percolation
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * https://github.com/alissonperez/coursera-percolation
 *
 * @author Alisson R. Perez
 */
public class Percolation {

	private int n ; // Size

	private boolean[] open_sites ; // Nós abertos.

	private WeightedQuickUnionUF uf ; // UnionFind implementation

	private int top_site ; // Nó superior que liga toda a linha superior do grid
	private int bottom_site ; // Nó inferior que liga toda a linha inferior do grid

	/**
	 * Create N-by-N grid, with all sites blocked
	 *
	 * @param  newN Tamanho
	 * @return
	 */
	public Percolation( int newN ) {
		n = newN ;
		initialize() ;
	}

	/**
	 * Inicializa
	 */
	private void initialize() {
		int n2 = n*n ;
		int i ;

		open_sites = new boolean[n2] ;
		for ( i = 0 ; i < n2 ; i++ ) {
			open_sites[i] = false ;
		}

		top_site = n2 ;
		bottom_site = n2 + 1 ;

		// Adiciona-se 2 para incluir o nó superior e inferior
		uf = new WeightedQuickUnionUF( n2+2 ) ;
		for ( i = 0 ; i < n ; i++ ) {
			uf.union( top_site , i ) ; // Linha superior
			uf.union( bottom_site , n2-i-1 ) ; // Linha inferior
		}
	}

	/**
	 * Open site (row i, column j) if it is not already
	 *
	 * @param i Row
	 * @param j Column
	 */
	public void open( int i , int j )  {
		if ( isOpen( i , j ) ) return ;

		open_sites[ getSiteIdx( i , j ) ] = true ;
		connectNeighborsIfOpen( i , j ) ;
	}

	/**
	 * Conecta o nó aos seus vizinhos, se estiveram abertos..
	 *
	 * @param i Linha
	 * @param j Coluna
	 */
	private void connectNeighborsIfOpen( int i , int j )  {
		int siteIdx = getSiteIdx( i , j ) ;

		// Baixo
		if ( i > 1 && isOpen( i-1 , j ) ) uf.union( siteIdx , getSiteIdx( i-1 , j ) ) ;

		// Cima
		if ( i < n && isOpen( i+1 , j ) ) uf.union( siteIdx , getSiteIdx( i+1 , j ) ) ;

		// Esquerda
		if ( j > 1 && isOpen( i , j-1 ) ) uf.union( siteIdx , getSiteIdx( i , j-1 ) ) ;

		// Direita
		if ( j < n && isOpen( i , j+1 ) ) uf.union( siteIdx , getSiteIdx( i , j+1 ) ) ;
	}

	/**
	 * Is site (row i, column j) open?
	 *
	 * @param  i Linha
	 * @param  j Coluna
	 * @return boolean
	 */
	public boolean isOpen( int i , int j )  {
		return open_sites[getSiteIdx( i , j )] ;
	}

	/**
	 * Is site (row i, column j) full? (Tem ligação com a linha superior?)
	 *
	 * @param  i Linha
	 * @param  j Coluna
	 * @return boolean
	 */
	public boolean isFull( int i , int j ) {
		int siteIdx = getSiteIdx( i , j ) ;
		return open_sites[siteIdx] && uf.connected( siteIdx , top_site ) ;
	}

	/**
	 * Retorna o índice para o elemento no array dadas as coordenadas.
	 *
	 * @param  i Linha
	 * @param  j Coluna
	 * @return int
	 */
	private int getSiteIdx( int i , int j ) {
		if ( i < 1 || i > n || j < 1 || j > n ) {
			throw new IndexOutOfBoundsException(
				"As coordenadas 'i' e 'j' devem estar dentro do grid (NxN)."
			) ;
		}

		// Transformar as cordenadas i e j no indíce do array
		return ( j-1 ) + ( i-1 ) * n ;
	}

	/**
	 * Does the system percolate?
	 *
	 * @return boolean
	 */
	public boolean percolates() {
		return uf.connected( top_site , bottom_site ) ;
	}

}