package PentagonalPrism;

import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Vertex3D;
import static java.lang.Math.*;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import graphicslib3D.*;
import static java.lang.Math.*;

public class PentaPrism
{
    private int numVertices, numIndices;
    private int[] indices;
    private Vertex3D[] vertices;
    private float[] penta_positions;
    private float[] penta_tex_coords;
    private float s;

    public PentaPrism(float s) {
        this.s = s;
        InitPentaPrism();
    }

    private void InitPentaPrism() {
        numVertices = 10;
        numIndices = 15;
        vertices = new Vertex3D[numVertices];
        indices = new int[numIndices];
        penta_positions = new float[144];
        penta_tex_coords = new float[96];

        for (int i = 0; i < numVertices; i++) {
            vertices[i] = new Vertex3D();
        }

        float s = 1.0f;
        float h = (float) ((1 / 2.) * Math.sqrt(5 + 2 * Math.sqrt(5)) * s);
        float Ax = 0;
        float Ay = -s / 2;
        float Az = h / 2.0f;
        float Bx = (float) ((s / 2.0f) + (s * Math.sin(Math.toRadians(18))));
        float By = -s / 2;
        float Bz = (float) ((-h / 2.0f) + (s * Math.cos(Math.toRadians(18))));
        float Cx = s / 2.0f;
        float Cy = -s / 2;
        float Cz = -h / 2.0f;
        float Dx = -s / 2.0f;
        float Dy = -s / 2;
        float Dz = -h / 2.0f;
        float Ex = (float) ((-s / 2.0f) - (s * Math.sin(Math.toRadians(18))));
        float Ey = -s / 2;
        float Ez = (float) ((-h / 2.0f) + (s * Math.cos(Math.toRadians(18))));
        float Gx = 0;
        float Gy = s / 2;
        float Gz = h / 2.0f;
        float Hx = (float) ((s / 2.0f) + (s * Math.sin(Math.toRadians(18))));
        float Hy = s / 2;
        float Hz = (float) ((-h / 2.0f) + (s * Math.cos(Math.toRadians(18))));
        float Ix = s / 2;
        float Iy = s / 2;
        float Iz = -h / 2.0f;
        float Jx = -s / 2.0f;
        float Jy = s / 2;
        float Jz = -h / 2.0f;
        float Kx = (float) (((-s / 2.0f) - (s * Math.sin(Math.toRadians(18)))));
        float Ky = s / 2;
        float Kz = (float) (((-h / 2.0f) + (s * Math.cos(Math.toRadians(18)))));
//        float[] penta_prism =  {
//                Ax, Ay, Az, Ex, Ey, Ez, Dx, Dy, Dz,     0.5,1   0,0.5,   0,0
//                Ax, Ay, Az, Dx, Dy, Dz, Cx, Cy, Cz,     0.5,1   0,0        1,0
//                Ax, Ay, Az, Cx, Cy, Cz, Bx, By, Bz,     0.5,1   1,0        1,0.5
//                Ax, Ay, Az, Gx, Gy, Gz, Hx, Hy, Hz,     1,0     1,1      0,1
//                Ax, Ay, Az, Bx, By, Bz, Hx, Hy, Hz,     1,0     0,0      0,1
//                Cx, Cy, Cz, Bx, By, Bz, Hx, Hy, Hz,     0,0     1,0      1,1
//                Ix, Iy, Iz, Cx, Cy, Cz, Hx, Hy, Hz,     0,1     0,0      1,1
//                Ix, Iy, Iz, Cx, Cy, Cz, Dx, Dy, Dz,     1,1     1,0      0,0
//                Ix, Iy, Iz, Dx, Dy, Dz, Jx, Jy, Jz,     1,1     0,0      0,1
//                Ex, Ey, Ez, Dx, Dy, Dz, Jx, Jy, Jz,     0,0     1,0      1,1
//                Ex, Ey, Ez, Kx, Ky, Kz, Jx, Jy, Jz,     0,0     0,1      1,1
//                Ex, Ey, Ez, Kx, Ky, Kz, Ax, Ay, Az,     1,0     1,1      0,0
//                Ax, Ay, Az, Kx, Ky, Kz, Gx, Gy, Gz,     0,0     1,1      0,1
//                Gx, Gy, Gz, Ix, Iy, Iz, Hx, Hy, Hz,     0.5,1   1,0      1,0.5
//                Gx, Gy, Gz, Ix, Iy, Iz, Jx, Jy, Jz,     0.5,1   1,0      0,0
//                Gx, Gy, Gz, Jx, Jy, Jz, Kx, Ky, Kz      0.5,1   0,0      0,0.5
//        };

        penta_tex_coords[0] = 0.5f; penta_tex_coords[1] = 1.0f; penta_tex_coords[2] = 0; penta_tex_coords[3] = 0.5f; penta_tex_coords[4] = 0; penta_tex_coords[5] = 0;
        penta_tex_coords[6] = 0.5f; penta_tex_coords[7] = 1.0f; penta_tex_coords[8] = 0; penta_tex_coords[9] = 0; penta_tex_coords[10] = 1; penta_tex_coords[11] = 0;
        penta_tex_coords[12] = 0.5f; penta_tex_coords[13] = 1.0f; penta_tex_coords[14] = 1; penta_tex_coords[15] = 0; penta_tex_coords[16] = 1; penta_tex_coords[17] = 0.5f;
        //                Ax, Ay, Az, Gx, Gy, Gz, Hx, Hy, Hz,     1,0     1,1      0,1
        penta_tex_coords[18] = 1; penta_tex_coords[19] = 0; penta_tex_coords[20] = 1; penta_tex_coords[21] = 1; penta_tex_coords[22] = 0; penta_tex_coords[23] = 1;
        //                Ax, Ay, Az, Bx, By, Bz, Hx, Hy, Hz,     1,0     0,0      0,1
        penta_tex_coords[24] = 1; penta_tex_coords[25] = 0; penta_tex_coords[26] = 0; penta_tex_coords[27] = 0; penta_tex_coords[28] = 0; penta_tex_coords[29] = 1;
        //                Cx, Cy, Cz, Bx, By, Bz, Hx, Hy, Hz,     0,0     1,0      1,1
        penta_tex_coords[30] = 0; penta_tex_coords[31] = 0; penta_tex_coords[32] = 1; penta_tex_coords[33] = 0; penta_tex_coords[34] = 1; penta_tex_coords[35] = 1;
        //                Ix, Iy, Iz, Cx, Cy, Cz, Hx, Hy, Hz,     0,1     0,0      1,1
        penta_tex_coords[36] = 0; penta_tex_coords[37] = 1; penta_tex_coords[38] = 0; penta_tex_coords[39] = 0; penta_tex_coords[40] = 1; penta_tex_coords[41] = 1;
        //                Ix, Iy, Iz, Cx, Cy, Cz, Dx, Dy, Dz,     1,1     1,0      0,0
        penta_tex_coords[42] = 1; penta_tex_coords[43] = 1; penta_tex_coords[44] = 1; penta_tex_coords[45] = 0; penta_tex_coords[46] = 0; penta_tex_coords[47] = 0;
        //                Ix, Iy, Iz, Dx, Dy, Dz, Jx, Jy, Jz,     1,1     0,0      0,1
        penta_tex_coords[48] = 1; penta_tex_coords[49] = 1; penta_tex_coords[50] = 0; penta_tex_coords[51] = 0; penta_tex_coords[52] = 0; penta_tex_coords[53] = 1;
        //                Ex, Ey, Ez, Dx, Dy, Dz, Jx, Jy, Jz,     0,0     1,0      1,1
        penta_tex_coords[54] = 0; penta_tex_coords[55] = 0; penta_tex_coords[56] = 1; penta_tex_coords[57] = 0; penta_tex_coords[58] = 1; penta_tex_coords[59] = 1;
        //                Ex, Ey, Ez, Kx, Ky, Kz, Jx, Jy, Jz,     0,0     0,1      1,1
        penta_tex_coords[60] = 0; penta_tex_coords[61] = 0; penta_tex_coords[62] = 0; penta_tex_coords[63] = 1; penta_tex_coords[64] = 1; penta_tex_coords[65] = 1;
        //                Ex, Ey, Ez, Kx, Ky, Kz, Ax, Ay, Az,     1,0     1,1      0,0
        penta_tex_coords[66] = 1; penta_tex_coords[67] = 0; penta_tex_coords[68] = 1; penta_tex_coords[69] = 1; penta_tex_coords[70] = 0; penta_tex_coords[71] = 0;
        //                Ax, Ay, Az, Kx, Ky, Kz, Gx, Gy, Gz,     0,0     1,1      0,1
        penta_tex_coords[72] = 0; penta_tex_coords[73] = 0; penta_tex_coords[74] = 1; penta_tex_coords[75] = 1; penta_tex_coords[76] = 0; penta_tex_coords[77] = 1;
        //                Gx, Gy, Gz, Ix, Iy, Iz, Hx, Hy, Hz,     0.5,1   1,0      1,0.5
        penta_tex_coords[78] = 0.5f; penta_tex_coords[79] = 1.0f; penta_tex_coords[80] = 1; penta_tex_coords[81] = 0; penta_tex_coords[82] = 1; penta_tex_coords[83] = 0.5f;
        //                Gx, Gy, Gz, Ix, Iy, Iz, Jx, Jy, Jz,     0.5,1   1,0      0,0
        penta_tex_coords[84] = 0.5f; penta_tex_coords[85] = 1.0f; penta_tex_coords[86] = 1; penta_tex_coords[87] = 0; penta_tex_coords[88] = 0; penta_tex_coords[89] = 0;
        //                Gx, Gy, Gz, Jx, Jy, Jz, Kx, Ky, Kz      0.5,1   0,0      0,0.5
        penta_tex_coords[90] = 0.5f; penta_tex_coords[91] = 1.0f; penta_tex_coords[92] = 0; penta_tex_coords[93] = 0; penta_tex_coords[94] = 0; penta_tex_coords[95] = 0.5f;


        penta_positions[0] = Ax; penta_positions[1] = Ay; penta_positions[2] = Az;
        penta_positions[3] = Ex; penta_positions[4] = Ey; penta_positions[5] = Ez;
        penta_positions[6] = Dx; penta_positions[7] = Dy; penta_positions[8] = Dz;
        penta_positions[9] = Ax; penta_positions[10] = Ay; penta_positions[11] = Az;
        penta_positions[12] = Dx; penta_positions[13] = Dy; penta_positions[14] = Dz;
        penta_positions[15] = Cx; penta_positions[16] = Cy; penta_positions[17] = Cz;
        penta_positions[18] = Ax;
        penta_positions[19] = Ay;
        penta_positions[20] = Az;
        penta_positions[21] = Cx;
        penta_positions[22] = Cy;
        penta_positions[23] = Cz;
        penta_positions[24] = Bx;
        penta_positions[25] = By;
        penta_positions[26] = Bz;
        penta_positions[27] = Ax;
        penta_positions[28] = Ay;
        penta_positions[29] = Az;
        penta_positions[30] = Gx;
        penta_positions[31] = Gy;
        penta_positions[32] = Gz;
        penta_positions[33] = Hx;
        penta_positions[34] = Hy;
        penta_positions[35] = Hz;
        penta_positions[36] = Ax;
        penta_positions[37] = Ay;
        penta_positions[38] = Az;
        penta_positions[39] = Bx;
        penta_positions[40] = By;
        penta_positions[41] = Bz;
        penta_positions[42] = Hx;
        penta_positions[43] = Hy;
        penta_positions[44] = Hz;
        penta_positions[45] = Cx;
        penta_positions[46] = Cy;
        penta_positions[47] = Cz;
        penta_positions[48] = Bx;
        penta_positions[49] = By;
        penta_positions[50] = Bz;
        penta_positions[51] = Hx;
        penta_positions[52] = Hy;
        penta_positions[53] = Hz;
        penta_positions[54] = Ix;
        penta_positions[55] = Iy;
        penta_positions[56] = Iz;
        penta_positions[57] = Cx;
        penta_positions[58] = Cy;
        penta_positions[59] = Cz;
        penta_positions[60] = Hx;
        penta_positions[61] = Hy;
        penta_positions[62] = Hz;
        penta_positions[63] = Ix;
        penta_positions[64] = Iy;
        penta_positions[65] = Iz;
        penta_positions[66] = Cx;
        penta_positions[67] = Cy;
        penta_positions[68] = Cz;
        penta_positions[69] = Dx;
        penta_positions[70] = Dy;
        penta_positions[71] = Dz;
        penta_positions[72] = Ix;
        penta_positions[73] = Iy;
        penta_positions[74] = Iz;
        penta_positions[75] = Dx;
        penta_positions[76] = Dy;
        penta_positions[77] = Dz;
        penta_positions[78] = Jx;
        penta_positions[79] = Jy;
        penta_positions[80] = Jz;
        penta_positions[81] = Ex;
        penta_positions[82] = Ey;
        penta_positions[83] = Ez;
        penta_positions[84] = Dx;
        penta_positions[85] = Dy;
        penta_positions[86] = Dz;
        penta_positions[87] = Jx;
        penta_positions[88] = Jy;
        penta_positions[89] = Jz;
        penta_positions[90] = Ex;
        penta_positions[91] = Ey;
        penta_positions[92] = Ez;
        penta_positions[93] = Kx;
        penta_positions[94] = Ky;
        penta_positions[95] = Kz;
        penta_positions[96] = Jx;
        penta_positions[97] = Jy;
        penta_positions[98] = Jz;
        penta_positions[99] = Ex;
        penta_positions[100] = Ey;
        penta_positions[101] = Ez;
        penta_positions[102] = Kx;
        penta_positions[103] = Ky;
        penta_positions[104] = Kz;
        penta_positions[105] = Ax;
        penta_positions[106] = Ay;
        penta_positions[107] = Az;
        penta_positions[108] = Ax;
        penta_positions[109] = Ay;
        penta_positions[110] = Az;
        penta_positions[111] = Kx;
        penta_positions[112] = Ky;
        penta_positions[113] = Kz;
        penta_positions[114] = Gx;
        penta_positions[115] = Gy;
        penta_positions[116] = Gz;
        penta_positions[117] = Gx;
        penta_positions[118] = Gy;
        penta_positions[119] = Gz;
        penta_positions[120] = Ix;
        penta_positions[121] = Iy;
        penta_positions[122] = Iz;
        penta_positions[123] = Hx;
        penta_positions[124] = Hy;
        penta_positions[125] = Hz;
        penta_positions[126] = Gx;
        penta_positions[127] = Gy;
        penta_positions[128] = Gz;
        penta_positions[129] = Ix;
        penta_positions[130] = Iy;
        penta_positions[131] = Iz;
        penta_positions[132] = Jx;
        penta_positions[133] = Jy;
        penta_positions[134] = Jz;
        penta_positions[135] = Gx;
        penta_positions[136] = Gy;
        penta_positions[137] = Gz;
        penta_positions[138] = Jx;
        penta_positions[139] = Jy;
        penta_positions[140] = Jz;
        penta_positions[141] = Kx;
        penta_positions[142] = Ky;
        penta_positions[143] = Kz;

    }

    public int[] getIndices()
    {	return indices;
    }

    public Vertex3D[] getVertices()
    {	return vertices;
    }

    public float[] getPositions()
    {	return penta_positions;
    }
    public float[] getTextureCoords() {
        return penta_tex_coords;
    }
}
