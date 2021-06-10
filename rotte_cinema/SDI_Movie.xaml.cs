using rotte_cinema.dao;
using rotte_cinema.vo;
using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace rotte_cinema
{
	/// <summary>
	/// SDI_Movie.xaml에 대한 상호 작용 논리
	/// </summary>
	public partial class SDI_Movie : Window
	{
		enum WEEK { 일, 월, 화, 수, 목, 금, 토 }
		DateTime reserveDate = DateTime.Today;

		Reserve reserve = new Reserve();
		public SDI_Movie()
		{
			InitializeComponent();
		}

		private void Window_Loaded(object sender, RoutedEventArgs e)
		{
			// 날짜 세팅
			gridDateSet();

			// 리스트를 리트스박스에 연결
			lsbMovieList.ItemsSource = getMovieData();
		}

		List<Movie> getMovieData()
		{
			List<Movie> _movies = new List<Movie>();
			Injection.ParserToObj(_movies, new Movie(), "SELECT *,IFNULL((SELECT SHOWING_INDEX FROM SHOWING B WHERE B.MOVIE_INDEX = A.MOVIE_INDEX limit 1),0) AS 'SHOWING_INDEX' FROM MOVIE A ORDER BY MOVIE_TITLE;;");

			foreach (var _movie in _movies)
			{
				// 상영 등급별 이미지 위치 지정
				_movie.movie_age_path = string.Format("Images/txt-age-small-{0}.png",_movie.movie_limit_age);

				// 상영하지 않는 영화는 흐리게 표시
				if(_movie.showing_index == 0)
				{
					_movie.movie_opacity = 0.5;
				}
				else
				{
					_movie.movie_opacity = 1;
				}
			}
			return _movies;
		}

		void gridDateSet()
		{
			gridDate.Children.Clear(); // 새로운 내용을 입력하기전에 그리드를 비워줌.
			//요일 생성
			for (int i = 0; i < 14; i++)
			{
				TextBlock txb = new TextBlock();

				txb.Text = $"{reserveDate.AddDays(i).Month}/{reserveDate.AddDays(i).Day} • {(WEEK)reserveDate.AddDays(i).DayOfWeek}";
				txb.FontSize = 16;
				Grid.SetColumn(txb, i);
				Grid.SetRow(txb, 0);
				txb.VerticalAlignment = VerticalAlignment.Center;
				txb.HorizontalAlignment = HorizontalAlignment.Center;
				txb.Uid = reserveDate.AddDays(i).ToShortDateString();

				// 요일별 색상 표시
				if (reserveDate.AddDays(i).DayOfWeek == DayOfWeek.Sunday)
				{
					txb.Foreground = Brushes.Red;
				}
				else if (reserveDate.AddDays(i).DayOfWeek == DayOfWeek.Saturday)
				{
					txb.Foreground = Brushes.Blue;
				}
				else
				{
					txb.Foreground = Brushes.Black;
				}

				// 오늘 이전의 날짜는 흐리게 표시.
				if(reserveDate.AddDays(i) < DateTime.Today)
				{
					txb.Opacity = 0.5;
				}
				else
				{
					txb.MouseDown += dateSelect;
				}

				gridDate.Children.Add(txb);
			}
		}

		void dateSelect(object sender, MouseButtonEventArgs e)
		{

			TextBlock txb = (TextBlock)sender;
			reserve.rDate = txb.Uid;


			MessageBox.Show(txb.Uid);
		}

		private void imgLeft_MouseDown(object sender, MouseButtonEventArgs e)
		{
			// 예약 날짜가 현재날짜보다 7일 미만이 아닐경우에만 이동.
			if (reserveDate > DateTime.Today.AddDays(-7))
			{
				reserveDate = reserveDate.AddDays(-7);
				gridDateSet();
			}
			
		}

		private void imgRight_MouseDown(object sender, MouseButtonEventArgs e)
		{
			// 예약 날짜가 현재날짜보다 14일 초과가 아닐경우에만 이동.
			if (reserveDate < DateTime.Today.AddDays(14))
			{
				reserveDate = reserveDate.AddDays(7);
				gridDateSet();
			}
		}

		private void imgCalendar_MouseDown(object sender, MouseButtonEventArgs e)
		{
			// 예약 날짜를 오늘로 변경
			reserveDate = DateTime.Today;
			gridDateSet();
		}

		private void lsbMovieList_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			if (lsbMovieList.SelectedItem != null)
			{
				MessageBox.Show($"선택된 영화는 {((Movie)lsbMovieList.SelectedItem).movie_title } 입니다 ");
			}
		}
	}
}
