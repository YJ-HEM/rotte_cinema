using System;
using System.Collections.Generic;
using System.Text;
using System.Windows;
using System.Windows.Controls;

namespace rotte_cinema.vo
{
	class Movie
	{
		public int movie_index { set; get; }
		public string movie_title { set; get; }
		public string movie_director { set; get; }
		public string movie_actor { set; get; }
		public string movie_genre { set; get; }
		public int movie_limit_age { set; get; }
		public int movie_running_time { set; get; }
		public string movie_open_date { set; get; }
		public string moive_end_date { set; get; }
		public string movie_poster { set; get; }
		public string movie_info { set; get; }
		public string movie_tags { set; get; }

		public Label lbl { set; get; }




		public void setMovie()
		{
			lbl = new Label();

			lbl.Name = "movieName";

			lbl.Content = movie_limit_age + movie_title;   // 버튼명
											
			lbl.FontSize = 14;

			lbl.HorizontalAlignment = HorizontalAlignment.Left;   // 정렬 기준 좌측
			lbl.VerticalAlignment = VerticalAlignment.Top;   // 정렬 기준 상측

		}

	}
}
