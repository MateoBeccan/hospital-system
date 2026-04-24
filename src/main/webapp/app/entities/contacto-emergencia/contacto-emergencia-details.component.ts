import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IContactoEmergencia } from '@/shared/model/contacto-emergencia.model';

import ContactoEmergenciaService from './contacto-emergencia.service';

export default defineComponent({
  name: 'ContactoEmergenciaDetails',
  setup() {
    const contactoEmergenciaService = inject('contactoEmergenciaService', () => new ContactoEmergenciaService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const contactoEmergencia: Ref<IContactoEmergencia> = ref({});

    const retrieveContactoEmergencia = async contactoEmergenciaId => {
      try {
        const res = await contactoEmergenciaService().find(contactoEmergenciaId);
        contactoEmergencia.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.contactoEmergenciaId) {
      retrieveContactoEmergencia(route.params.contactoEmergenciaId);
    }

    return {
      alertService,
      contactoEmergencia,

      previousState,
    };
  },
});
