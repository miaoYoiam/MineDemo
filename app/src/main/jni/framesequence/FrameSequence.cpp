/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "FrameSequence.h"

#include "Registry.h"

FrameSequence* FrameSequence::create(Stream* stream) {
    const RegistryEntry* entry = Registry::Find(stream);

    if (!entry) return NULL;

    FrameSequence* frameSequence = entry->createFrameSequence(stream);
    if (!frameSequence->getFrameCount() ||
            !frameSequence->getWidth() || !frameSequence->getHeight()) {
        // invalid contents, abort
        delete frameSequence;
        return NULL;
    }

    return frameSequence;
}

bool FrameSequence::isSupport(Stream* stream) {
    bool isSupport = false;

    const RegistryEntry* entry = Registry::Find(stream);

    if (!entry) return isSupport;

    //有需要检查内容的自行打开
    //skip content check
    /*
    FrameSequence* frameSequence = entry->createFrameSequence(stream);
    if (!frameSequence->getFrameCount() ||
            !frameSequence->getWidth() || !frameSequence->getHeight()) {
        return isSupport;
    }

    delete frameSequence;
    */

    isSupport = true;
    return isSupport;
}
