/*Copyright (C) 2012  Jonah Murphy

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */


package gifty.core;

public class NumericAnswer extends Answer{

	private int tolerance;

	public NumericAnswer(String answer, String feedback, int tolerance, int mark) {
		super(answer, feedback, mark);
		this.tolerance = tolerance;
	}
	
	public NumericAnswer(String answer, int tolerance, int mark) {
		//no feedback..
		super(answer, "", mark);
		this.tolerance = tolerance;
		
	}
	
	public int getTolerance() {
		return tolerance;
	}
}
