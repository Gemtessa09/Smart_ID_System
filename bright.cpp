#include <iostream>
using namespace std;

int main() {
  int n, m, i, j;
  int Sum = 0;  // Initialize Sum to 0

  m = 2;
  n = 3;

  // First Loop: Calculating the sum of 1 to n
  for (i = 1; i <= n; i++) { 
    Sum = Sum + i; 
  }

  // Second Loop:  Incrementing Sum n*m times
  for (i = 1; i <= n; i++) {
    for (j = 1; j <= m; j++) {
      Sum++; 
    }
  }

  cout << "Final Sum: " << Sum << endl; // Print the result
  return 0;
}



