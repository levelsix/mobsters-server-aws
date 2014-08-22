// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: NoneventDev.proto

package com.lvl6.mobsters.noneventproto;

public final class NoneventDevProto {
  private NoneventDevProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum DevRequest
      implements com.google.protobuf.ProtocolMessageEnum {
    RESET_ACCOUNT(0, 1),
    GET_MONZTER(1, 2),
    F_B_GET_CASH(2, 3),
    F_B_GET_OIL(3, 4),
    F_B_GET_GEMS(4, 5),
    F_B_GET_CASH_OIL_GEMS(5, 6),
    ;
    
    public static final int RESET_ACCOUNT_VALUE = 1;
    public static final int GET_MONZTER_VALUE = 2;
    public static final int F_B_GET_CASH_VALUE = 3;
    public static final int F_B_GET_OIL_VALUE = 4;
    public static final int F_B_GET_GEMS_VALUE = 5;
    public static final int F_B_GET_CASH_OIL_GEMS_VALUE = 6;
    
    
    public final int getNumber() { return value; }
    
    public static DevRequest valueOf(int value) {
      switch (value) {
        case 1: return RESET_ACCOUNT;
        case 2: return GET_MONZTER;
        case 3: return F_B_GET_CASH;
        case 4: return F_B_GET_OIL;
        case 5: return F_B_GET_GEMS;
        case 6: return F_B_GET_CASH_OIL_GEMS;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<DevRequest>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<DevRequest>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<DevRequest>() {
            public DevRequest findValueByNumber(int number) {
              return DevRequest.valueOf(number);
            }
          };
    
    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.lvl6.mobsters.noneventproto.NoneventDevProto.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final DevRequest[] VALUES = {
      RESET_ACCOUNT, GET_MONZTER, F_B_GET_CASH, F_B_GET_OIL, F_B_GET_GEMS, F_B_GET_CASH_OIL_GEMS, 
    };
    
    public static DevRequest valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private DevRequest(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:proto.DevRequest)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\021NoneventDev.proto\022\005proto*\200\001\n\nDevReques" +
      "t\022\021\n\rRESET_ACCOUNT\020\001\022\017\n\013GET_MONZTER\020\002\022\020\n" +
      "\014F_B_GET_CASH\020\003\022\017\n\013F_B_GET_OIL\020\004\022\020\n\014F_B_" +
      "GET_GEMS\020\005\022\031\n\025F_B_GET_CASH_OIL_GEMS\020\006B3\n" +
      "\037com.lvl6.mobsters.noneventprotoB\020Noneve" +
      "ntDevProto"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
