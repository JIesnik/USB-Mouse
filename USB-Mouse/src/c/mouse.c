#include <iostream>
#include <Windows.h>

using namespace std;

int main()
{
	while(!cin.eof()) {
		char str[500];
		int x,y,scrollnum;
		cin >> str;
		if(strstr(str, "SNSCOORD") != NULL) { 
			char *buffer;
			buffer = strtok(str,":");
				buffer = strtok(NULL,":");
				x = atoi(buffer);
				buffer = strtok(NULL,":");
				y = atoi(buffer);
				buffer = strtok(NULL,":");


				POINT pos;
				GetCursorPos(&pos);
				SetCursorPos(pos.x+x, pos.y+y);	
		}
		else if(strstr(str, "SNSSCOORD") != NULL) {
			char *buffer;
			buffer = strtok(str,":");
			buffer = strtok(NULL,":");
			scrollnum = atoi(buffer);
			buffer = strtok(NULL,":");

			POINT pos;
			GetCursorPos(&pos);
			mouse_event(MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_WHEEL, pos.x, pos.y, scrollnum, 0); 

		}
		else if(strstr(str, "SNSTAP") != NULL) {
			POINT pos;
			GetCursorPos(&pos);
			mouse_event(MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_LEFTDOWN, pos.x, pos.y, 0, 0); 
			mouse_event(MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_LEFTUP, pos.x, pos.y, 0, 0);

		}
		else if(strstr(str, "SNSDOWN") != NULL) {
			POINT pos;
			GetCursorPos(&pos);
			mouse_event(MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_LEFTDOWN, pos.x, pos.y, 0, 0); 

		}
		else if(strstr(str, "SNSUP") != NULL) {
			POINT pos;
			GetCursorPos(&pos);
			mouse_event(MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_LEFTUP, pos.x, pos.y, 0, 0); 

		}
		else if(strstr(str, "SNSDTAP") != NULL) {
			POINT pos;
			GetCursorPos(&pos);
			
			mouse_event(MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_RIGHTDOWN, pos.x, pos.y, 0, 0); 
			mouse_event(MOUSEEVENTF_ABSOLUTE|MOUSEEVENTF_RIGHTUP, pos.x, pos.y, 0, 0);
		}
	}
	return 0;
}

