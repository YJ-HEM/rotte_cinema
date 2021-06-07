using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Text;
using System.Windows;
using System.Windows.Controls;

namespace rotte_cinema.vo
{
	class Seat
	{
		public int theater_index { set; get; }
		public int seat_x { set; get; }
		public int seat_y { set; get; }
		public int seat_type { set; get; }
		public Button btn { set; get; }

		private int margin_top = 50;
		private int margin_left = 10;
		private int btn_size = 30;


		public void setSeatBtn()
		{
			btn = new Button();
			btn.Content = seat_x + "/" + seat_y;   // 버튼명
			btn.Width = btn_size;
			btn.Height = btn_size;
			btn.HorizontalAlignment = HorizontalAlignment.Left;   // 정렬 기준 좌측
			btn.VerticalAlignment = VerticalAlignment.Top;   // 정렬 기준 상측
			btn.Margin = new Thickness((btn.Width * seat_x) + margin_left, (btn.Height * seat_y) + margin_top, 0, 0);   // 위치
			btn.Click += Button_Click;
		}

		void Button_Click(object sender, RoutedEventArgs e)
		{
			MessageBox.Show(string.Format("가로 {0} / 세로 {1} 좌석입니다.", seat_x.ToString(), seat_y.ToString()));
		}
	}
}
