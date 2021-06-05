using rotte_cinema.dao;
using rotte_cinema.vo;
using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;


namespace rotte_cinema
{
	/// <summary>
	/// Interaction logic for MainWindow.xaml
	/// </summary>
	public partial class MainWindow : Window
	{
		List<Cinema> cinema = new List<Cinema>();
		List<Movie> movie = new List<Movie>();
		public MainWindow()
		{
			InitializeComponent();

			// SQL 초기 연결 정보를 설정
			MySqlManager.setConnection();
		}

		
		private void Grid_Loaded(object sender, RoutedEventArgs e)
		{

			// 영화관 정보 호출
			Injection.ParserToObj(cinema, new Cinema(), "SELECT * FROM CINEMA");
			for (int i = 0; i < cinema.Count; i++)
			{
				Debug.WriteLine(cinema[i].cinema_index);
				Debug.WriteLine(cinema[i].cinema_title);
				Debug.WriteLine(cinema[i].cinema_address);
				Debug.WriteLine(cinema[i].cinema_info);
			}

			
			Injection.ParserToObj(movie, new Movie(), "SELECT * FROM MOVIE");
			for (int i = 0; i < movie.Count; i++)
			{
				Debug.WriteLine(movie[i].movie_index);
				Debug.WriteLine(movie[i].movie_title);
				Debug.WriteLine(movie[i].movie_director);
				Debug.WriteLine(movie[i].movie_actor);
				Debug.WriteLine(movie[i].movie_genre);
				Debug.WriteLine(movie[i].movie_limit_age);
				Debug.WriteLine(movie[i].movie_running_time);
				Debug.WriteLine(movie[i].movie_open_date);
				Debug.WriteLine(movie[i].moive_end_date);
				Debug.WriteLine(movie[i].movie_poster);
				Debug.WriteLine(movie[i].movie_info);
				Debug.WriteLine(movie[i].movie_tags);
			}
			
		}
	}
}
