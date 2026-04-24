<template>
  <div class="d-flex justify-content-center">
    <div class="col-8">
      <form name="editForm" novalidate @submit.prevent="save()">
        <h2 id="hospitalApp.persona.home.createOrEditLabel" data-cy="PersonaCreateUpdateHeading">Crear o editar Persona</h2>
        <div>
          <div class="mb-3" v-if="persona.id">
            <label for="id">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="persona.id" readonly />
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Nombre</label>
            <input
              type="text"
              class="form-control"
              name="nombre"
              id="persona-nombre"
              data-cy="nombre"
              :class="{ valid: !v$.nombre.$invalid, invalid: v$.nombre.$invalid }"
              v-model="v$.nombre.$model"
              required
            />
            <div v-if="v$.nombre.$anyDirty && v$.nombre.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nombre.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Apellido</label>
            <input
              type="text"
              class="form-control"
              name="apellido"
              id="persona-apellido"
              data-cy="apellido"
              :class="{ valid: !v$.apellido.$invalid, invalid: v$.apellido.$invalid }"
              v-model="v$.apellido.$model"
              required
            />
            <div v-if="v$.apellido.$anyDirty && v$.apellido.$invalid">
              <small class="form-text text-danger" v-for="error of v$.apellido.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Nro Documento</label>
            <input
              type="text"
              class="form-control"
              name="nroDocumento"
              id="persona-nroDocumento"
              data-cy="nroDocumento"
              :class="{ valid: !v$.nroDocumento.$invalid, invalid: v$.nroDocumento.$invalid }"
              v-model="v$.nroDocumento.$model"
              required
            />
            <div v-if="v$.nroDocumento.$anyDirty && v$.nroDocumento.$invalid">
              <small class="form-text text-danger" v-for="error of v$.nroDocumento.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Fecha Nacimiento</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="persona-fechaNacimiento"
                  v-model="v$.fechaNacimiento.$model"
                  name="fechaNacimiento"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="persona-fechaNacimiento"
                data-cy="fechaNacimiento"
                type="text"
                class="form-control"
                name="fechaNacimiento"
                :class="{ valid: !v$.fechaNacimiento.$invalid, invalid: v$.fechaNacimiento.$invalid }"
                v-model="v$.fechaNacimiento.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Telefono</label>
            <input
              type="text"
              class="form-control"
              name="telefono"
              id="persona-telefono"
              data-cy="telefono"
              :class="{ valid: !v$.telefono.$invalid, invalid: v$.telefono.$invalid }"
              v-model="v$.telefono.$model"
            />
            <div v-if="v$.telefono.$anyDirty && v$.telefono.$invalid">
              <small class="form-text text-danger" v-for="error of v$.telefono.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="persona-email"
              data-cy="email"
              :class="{ valid: !v$.email.$invalid, invalid: v$.email.$invalid }"
              v-model="v$.email.$model"
            />
            <div v-if="v$.email.$anyDirty && v$.email.$invalid">
              <small class="form-text text-danger" v-for="error of v$.email.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Direccion</label>
            <input
              type="text"
              class="form-control"
              name="direccion"
              id="persona-direccion"
              data-cy="direccion"
              :class="{ valid: !v$.direccion.$invalid, invalid: v$.direccion.$invalid }"
              v-model="v$.direccion.$model"
            />
            <div v-if="v$.direccion.$anyDirty && v$.direccion.$invalid">
              <small class="form-text text-danger" v-for="error of v$.direccion.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Activo</label>
            <input
              type="checkbox"
              class="form-check"
              name="activo"
              id="persona-activo"
              data-cy="activo"
              :class="{ valid: !v$.activo.$invalid, invalid: v$.activo.$invalid }"
              v-model="v$.activo.$model"
              required
            />
            <div v-if="v$.activo.$anyDirty && v$.activo.$invalid">
              <small class="form-text text-danger" v-for="error of v$.activo.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Fecha Alta</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="persona-fechaAlta"
                  v-model="v$.fechaAlta.$model"
                  name="fechaAlta"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="persona-fechaAlta"
                data-cy="fechaAlta"
                type="text"
                class="form-control"
                name="fechaAlta"
                :class="{ valid: !v$.fechaAlta.$invalid, invalid: v$.fechaAlta.$invalid }"
                v-model="v$.fechaAlta.$model"
                required
              />
            </b-input-group>
            <div v-if="v$.fechaAlta.$anyDirty && v$.fechaAlta.$invalid">
              <small class="form-text text-danger" v-for="error of v$.fechaAlta.$errors" :key="error.$uid">{{ error.$message }}</small>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Fecha Baja</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="persona-fechaBaja"
                  v-model="v$.fechaBaja.$model"
                  name="fechaBaja"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="persona-fechaBaja"
                data-cy="fechaBaja"
                type="text"
                class="form-control"
                name="fechaBaja"
                :class="{ valid: !v$.fechaBaja.$invalid, invalid: v$.fechaBaja.$invalid }"
                v-model="v$.fechaBaja.$model"
              />
            </b-input-group>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Tipo Documento</label>
            <select
              class="form-control"
              id="persona-tipoDocumento"
              data-cy="tipoDocumento"
              name="tipoDocumento"
              v-model="persona.tipoDocumento"
              required
            >
              <option v-if="!persona.tipoDocumento" :value="null" selected></option>
              <option
                :value="
                  persona.tipoDocumento && tipoDocumentoOption.id === persona.tipoDocumento.id ? persona.tipoDocumento : tipoDocumentoOption
                "
                v-for="tipoDocumentoOption in tipoDocumentos"
                :key="tipoDocumentoOption.id"
              >
                {{ tipoDocumentoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.tipoDocumento.$anyDirty && v$.tipoDocumento.$invalid">
            <small class="form-text text-danger" v-for="error of v$.tipoDocumento.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Sexo</label>
            <select class="form-control" id="persona-sexo" data-cy="sexo" name="sexo" v-model="persona.sexo" required>
              <option v-if="!persona.sexo" :value="null" selected></option>
              <option
                :value="persona.sexo && sexoOption.id === persona.sexo.id ? persona.sexo : sexoOption"
                v-for="sexoOption in sexos"
                :key="sexoOption.id"
              >
                {{ sexoOption.id }}
              </option>
            </select>
          </div>
          <div v-if="v$.sexo.$anyDirty && v$.sexo.$invalid">
            <small class="form-text text-danger" v-for="error of v$.sexo.$errors" :key="error.$uid">{{ error.$message }}</small>
          </div>
          <div class="mb-3">
            <label class="form-control-label" for="persona">Ciudad</label>
            <select class="form-control" id="persona-ciudad" data-cy="ciudad" name="ciudad" v-model="persona.ciudad">
              <option :value="null"></option>
              <option
                :value="persona.ciudad && ciudadOption.id === persona.ciudad.id ? persona.ciudad : ciudadOption"
                v-for="ciudadOption in ciudads"
                :key="ciudadOption.id"
              >
                {{ ciudadOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" @click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span>Cancelar</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span>Guardar</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./persona-update.component.ts"></script>
