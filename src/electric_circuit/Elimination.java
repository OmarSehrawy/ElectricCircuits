package electric_circuit;

public class Elimination {
    public static void RowSwap(double[][] A,double[] B,int r1, int r2) {
        double[] tempRow = A[r1];
        A[r1] = A[r2];
        A[r2] = tempRow;
        double temp = B[r1];
        B[r1] = B[r2];
        B[r2] = temp;
    }
    public static void Solve(double[][] A,double[] B) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            int max = i;
            for (int k = i+1; k < n; k++) {
                if (Math.abs(A[k][i]) > Math.abs(A[max][i])) max = k;
            }
            RowSwap(A,B,i,max);

            if (Math.abs(A[i][i]) < 1e-10) continue;

            for (int j = i+1; j < n; j++) {
                double factor = A[j][i] / A[i][i];
                B[j] -= factor * B[i];
                for (int k = i; k < n; k++) {
                    A[j][k] -= factor * A[i][k];
                }
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (Math.abs(A[i][i]) < 1e-10) continue;

            for (int j = i-1; j >= 0; j--) {
                double factor = A[j][i] / A[i][i];
                B[j] -= factor * B[i];
                A[j][i] = 0;
            }

            B[i] /= A[i][i];
            A[i][i] = 1;
        }
    }
}