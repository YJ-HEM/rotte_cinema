using rotte_cinema.dao;
using rotte_cinema.vo;
using System;
using System.Collections.Generic;
using System.Data;
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
        List<Cinema> cinema = new List<Cinema>();
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

            lsbMovieList.Items.Clear();

            // 리스트를 리트스박스에 연결
            lsbMovieList.ItemsSource = getMovieData();

            Injection.ParserToObj(cinema, new Cinema(), "SELECT * FROM CINEMA");
        }

        List<Movie> getMovieData()
        {
            List<Movie> _movies = new List<Movie>();
            Injection.ParserToObj(_movies, new Movie(), "SELECT *,IFNULL((SELECT SHOWING_INDEX FROM SHOWING B WHERE B.MOVIE_INDEX = A.MOVIE_INDEX limit 1),0) AS 'SHOWING_INDEX' FROM MOVIE A ORDER BY MOVIE_TITLE;;");

            foreach (var _movie in _movies)
            {
                // 상영 등급별 이미지 위치 지정
                _movie.movie_age_path = string.Format("Images/txt-age-small-{0}.png", _movie.movie_limit_age);

                // 상영하지 않는 영화는 흐리게 표시
                if (_movie.showing_index == 0)
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
                if (reserveDate.AddDays(i) < DateTime.Today)
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
			//txb.Background = Brushes.White;
			reserve.rDate = txb.Uid;

            txb.Background = Brushes.LightGray; //다른 날짜 선택하면 색상 원래대로 돌려야함
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
                selectedMovie.Text = ((Movie)lsbMovieList.SelectedItem).movie_title;
                lsbCinemaList.Items.Clear();
                //int a = setCinema(makeCombo(), 0);
                //setCinema(makeCombo(), a);

                setLocal(makeLabel()); //for문 돌려야함??
                setCinema(makeCombo());

                setLocal(makeLabel());
                setCinema(makeCombo());

            }
        }

        ComboBox makeCombo()
        {
            ComboBox cmb = new ComboBox();
            cmb.VerticalAlignment = VerticalAlignment.Top;
            //cmb.Height = 30;
            //cmb.Margin = new Thickness(0, 60 * idxCinema + 30, 0, 0);
            return cmb;
        }

        Label makeLabel()
        {
            Label lbl = new Label();
            lbl.VerticalAlignment = VerticalAlignment.Top;
            return lbl;
        }

        int idxLocal = 0;

        void setLocal(Label labl)
        {
            //labl.Margin = new Thickness(0, idxLocal * 60, 0, 0);
            DataTable result = MySqlManager.AdapterRead("SELECT CATEGORY_LOCAL.LOCAL_NAME from CATEGORY_LOCAL, CINEMA where CINEMA.LOCAL_INDEX = CATEGORY_LOCAL.LOCAL_INDEX");


            for (int i = idxLocal; i < result.Rows.Count; i++)
            {
                if (i != result.Rows.Count - 1 && !result.Rows[i][0].Equals(result.Rows[i + 1][0])) { labl.Content = result.Rows[i][0].ToString(); idxLocal = i + 1; break; }
                else if (i == result.Rows.Count - 1 && !result.Rows[i][0].Equals(result.Rows[i - 1][0])) { labl.Content = result.Rows[i][0].ToString(); idxLocal = 0; }
                else if (i == result.Rows.Count - 1 && result.Rows[i][0].Equals(result.Rows[i - 1][0])) { labl.Content = result.Rows[i][0].ToString(); idxLocal = 0; }

            }

            lsbCinemaList.Items.Add((Label)labl);
        }


        int idxCinema = 0;
        string index = "";

        //int setCinema(ComboBox combo, int index)
        ComboBox setCinema(ComboBox combo)
        {
            combo.FontSize = 16;
            for (int i = idxCinema; i < cinema.Count; i++)
            {
                idxCinema++;

                combo.Items.Add(cinema[i].cinema_title);

                if (i != cinema.Count - 1 && !cinema[i].local_index.Equals(cinema[i + 1].local_index))
                {
                    break;
                }
                if (i == cinema.Count - 1) { idxCinema = 0; }
            }

            combo.SelectedIndex = 0;
            lsbCinemaList.Items.Add(combo);
            //return idxCinema + 1;
            index = combo.SelectedItem.ToString();
            
            combo.SelectionChanged += combo_SelectedIndexChanged;
            return combo;
        }


        void combo_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (reserve.rDate == null) {
                MessageBox.Show("날짜를 선택하세요");
            }
            List<Showing> showing = new List<Showing>();

            int movieIndex = ((Movie)lsbMovieList.SelectedItem).movie_index;

          
            string sql = string.Format("SELECT * FROM SHOWING WHERE SHOWING.MOVIE_INDEX = {0} AND SHOWING.SHOW_DATE = '{1}' AND   (select THEATER.CINEMA_INDEX from THEATER where SHOWING.THEATER_INDEX= THEATER.THEATER_INDEX)  = (select CINEMA.CINEMA_INDEX from CINEMA where CINEMA_TITLE= '{2}')", movieIndex, reserve.rDate, index);
            Injection.ParserToObj(showing, new Showing(), sql);


            if (showing.Count==0)
            {
                lsbShowing.Items.Add(string.Format($"{reserve.rDate}의 {((Movie)lsbMovieList.SelectedItem).movie_title} 상영 정보 없음"));
            }


            foreach (var items in showing) {
                

                lsbShowing.Items.Add("상영관" + items.theater_index);
                lsbShowing.Items.Add(items.show_starttime);
                selectedCinema.Text = index;
            }


        }
    } 
}
