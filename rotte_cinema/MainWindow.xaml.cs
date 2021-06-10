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
		List<Seat> seat = new List<Seat>();

		public MainWindow()
		{
			InitializeComponent();

			// SQL 초기 연결 정보를 설정
			MySqlManager.setConnection();
		}


		private void Grid_Loaded(object sender, RoutedEventArgs e)
		{
			Injection.ParserToObj(cinema, new Cinema(), "SELECT * FROM CINEMA");

			/*
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
			*/

			//cmb_index.Items.Add("강남");
			//cmb_index.Items.Add("칠성로");
			//cmb_index.Items.Add("동대구");

			movieSet();
			SDI_Movie f = new SDI_Movie();
			f.Show();
		}
		/*
		void seatSet(int theater_index)
		{
			Injection.ParserToObj(seat, new Seat(), "SELECT * FROM SEAT");
			for (int i = 0; i < seat.Count; i++)
			{
				grid.Children.Remove(seat[i].btn);
				
				if (seat[i].theater_index == theater_index)
				{
					seat[i].setSeatBtn();
					grid.Children.Add(seat[i].btn);
				}				
			}
		}

		private void cmb_index_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			int theater_index = Convert.ToInt32(cmb_index.SelectedItem);
			seatSet(theater_index);
		}
		*/


		void movieSet()
		{
			/*
			Injection.ParserToObj(movie, new Movie(), "SELECT * FROM MOVIE");
			List<Label> movieLbl = new List<Label>();
			for (int i = 0; i < movie.Count; i++)
			{
				movie[i].setMovie();
				movieLbl.Add(movie[i].lbl);
			}
			MNamebox.ItemsSource = movieLbl;
			MNamebox.SelectionChanged += listbox_selectionchaged;
			*/

		}


		void listbox_selectionchaged(object sender, SelectionChangedEventArgs e) {
			calenderSet();
		}






		void calenderSet()
		{
			Calendar cal = new Calendar();
			cal.SelectionMode = CalendarSelectionMode.MultipleRange;
			DateTime nowdate = DateTime.Today;
			DateTime afterweek = nowdate.AddDays(+7);
			cal.SelectedDates.Add(DateTime.Today);
			cal.SelectedDates.AddRange(nowdate, afterweek);
			CalG.Children.Add(cal);
			cal.SelectedDatesChanged += cal_SelectedDatesChanged;
		}

		private void cal_SelectedDatesChanged(object sender, SelectionChangedEventArgs e)
		{
			cinemaG.Children.Clear();
			int a = setCinema(makeCombo(), 0);
			setCinema(makeCombo(),a );
			
			setLocal(makeLabel());
			setLocal(makeLabel());
		}

		ComboBox makeCombo()
		{
			ComboBox cmb = new ComboBox();
			cmb.VerticalAlignment = VerticalAlignment.Top;
			cmb.Height = 30;
			cmb.Margin = new Thickness(0, 60*idxCinema+30, 0, 0);
			return cmb;
		}

		Label makeLabel() {
			Label lbl = new Label();
            lbl.VerticalAlignment = VerticalAlignment.Top;
			return lbl;
		}

		int idxLocal = 0;
		
		void setLocal(Label labl) {


			labl.Margin = new Thickness(0, idxLocal * 60, 0,0);
			DataTable result = MySqlManager.AdapterRead("SELECT CATEGORY_LOCAL.LOCAL_NAME from CATEGORY_LOCAL, CINEMA where CINEMA.LOCAL_INDEX = CATEGORY_LOCAL.LOCAL_INDEX");

			
			for (int i = idxLocal; i < result.Rows.Count; i++)
            {	
				if ( i!=result.Rows.Count-1 && !result.Rows[i][0].Equals(result.Rows[i+1][0])) { labl.Content = result.Rows[i][0].ToString(); idxLocal = i + 1; break; }
				else if(i == result.Rows.Count-1 && !result.Rows[i][0].Equals(result.Rows[i - 1][0])) { labl.Content = result.Rows[i][0].ToString(); idxLocal = 0; }
				else if (i == result.Rows.Count - 1 && result.Rows[i][0].Equals(result.Rows[i - 1][0])) { labl.Content = result.Rows[i][0].ToString(); idxLocal = 0; }

			}


			cinemaG.Children.Add((Label)labl);
		}


		int idxCinema = 0;


		int setCinema(ComboBox combo,int index)
        {

			for (int i = idxCinema; i<cinema.Count ; i++)
			{
				idxCinema++;

				combo.Items.Add(cinema[i].cinema_title);


				
				if (i!=cinema.Count-1 &&  !cinema[i].local_index.Equals(cinema[i + 1].local_index))
				{
					break;
				}
				if (i == cinema.Count - 1) { idxCinema = 0; }
			}

			combo.SelectedIndex = 0;
			cinemaG.Children.Add(combo);
			return idxCinema+1;
        }

    }
}
