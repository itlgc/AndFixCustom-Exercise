//
// Created by LGC on 2020-03-21.
//


namespace art7_0 {
    namespace mirror {
        class Object {
        public:
            // The number of vtable entries in java.lang.Object.
            static uint32_t hash_code_seed;
            uint32_t klass_;

            uint32_t monitor_;
        };



        class ArtMethod {
        public:

            // Field order required by test "ValidateFieldOrderOfJavaCppUnionClasses".
            // The class we are a part of.
            uint32_t declaring_class_;
            // Access flags; low 16 bits are defined by spec.
            uint32_t access_flags_;
            /* Dex file fields. The defining dex file is available via declaring_class_->dex_cache_ */
            // Offset to the CodeItem.
            uint32_t dex_code_item_offset_;
            // Index into method_ids of the dex file associated with this method.
            uint32_t dex_method_index_;
            /* End of dex file fields. */
            // Entry within a dispatch table for this method. For static/direct methods the index is into
            // the declaringClass.directMethods, for virtual methods the vtable and for interface methods the
            // ifTable.
            uint16_t method_index_;

            // The hotness we measure for this method. Incremented by the interpreter. Not atomic, as we allow
            // missing increments: if the method is hot, we will see it eventually.
            uint16_t hotness_count_;
        };

    }

}
