import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type IMedico } from '@/shared/model/medico.model';

import MedicoService from './medico.service';

export default defineComponent({
  name: 'MedicoDetails',
  setup() {
    const medicoService = inject('medicoService', () => new MedicoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const medico: Ref<IMedico> = ref({});

    const retrieveMedico = async medicoId => {
      try {
        const res = await medicoService().find(medicoId);
        medico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.medicoId) {
      retrieveMedico(route.params.medicoId);
    }

    return {
      alertService,
      medico,

      previousState,
    };
  },
});
