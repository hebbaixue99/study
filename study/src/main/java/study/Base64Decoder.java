package study;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

 

class Base64Decoder
{
   
   private static  int ESCAPE_CHAR_CODE  = 61;
   
   private static  int[] inverse = {64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,62,64,64,64,63,52,53,54,55,56,57,58,59,60,61,64,64,64,64,64,64,64,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,64,64,64,64,64,64,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64,64};
    
   
   private  int count = 0;
   
   private ByteArrayOutputStream data =new ByteArrayOutputStream();
   
   private int filled = 0;
   
   private  int[] work={0,0,0,0};
   
   public Base64Decoder()
   {
     // this.work = {0,0,0,0};
     // super();
     // this.data = new ByteArray();
   }
   
  /* private void copyByteArray(byte[] param1, byte[] param2, int param3 )  
   {
	   data.
       int _loc4_ = param1.position;
      param1.position = 0;
      param2.position = 0;
      var _loc5_:uint = 0;
      while(param1.bytesAvailable > 0 && _loc5_ < param3)
      {
         param2.writeByte(param1.readByte());
         _loc5_++;
      }
      param1.position = _loc4_;
      param2.position = 0;
   }
   */
   public void decode(String param1)  
   {
		  
      int _loc3_  = 0;
      int _loc2_ = 0;
      while(_loc2_ < param1.length())
      {
         _loc3_ = param1.charAt(_loc2_); //.charCodeAt(_loc2_);
         if(_loc3_ == ESCAPE_CHAR_CODE)
         {
            this.work[this.count++] = -1;
           // addr65:
            if(this.count == 4)
            {
               this.count = 0;
               this.data.write((this.work[0] << 2 | (this.work[1] & 255) >> 4));
               this.filled++;
               if(this.work[2] == -1)
               {
                  break;
               }
               this.data.write(this.work[1] << 4 | (this.work[2] & 255) >> 2);
               this.filled++;
               if(this.work[3] == -1)
               {
                  break;
               }
               this.data.write(this.work[2] << 6 | this.work[3]);
               this.filled++;
            }
         }
         else if(inverse[_loc3_] != 64)
         {
            this.work[this.count++] = inverse[_loc3_];
			   if(this.count == 4)
			   {
				   this.count = 0;
				   this.data.write(this.work[0] << 2 | (this.work[1] & 255) >> 4);
				   this.filled++;
				   if(this.work[2] == -1)
				   {
					   break;
				   }
				   this.data.write(this.work[1] << 4 | (this.work[2] & 255) >> 2);
				   this.filled++;
				   if(this.work[3] == -1)
				   {
					   break;
				   }
				   this.data.write(this.work[2] << 6 | this.work[3]);
				   this.filled++;
			   }
            //§§goto(addr65);
         }
         _loc2_++;
      }
   }
   
   public byte[] drain()
   {
      //ByteBuffer _loc1_ =   ByteBuffer.allocate(1024);
      //_loc1_.put(0, this.data); //(this.data,_loc1_,this.filled);
     // _loc1_.wrap(this.data.toByteArray());
     // this.filled = 0;
      return this.data.toByteArray();
   }
 
}
